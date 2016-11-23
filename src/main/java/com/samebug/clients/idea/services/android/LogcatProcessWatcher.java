package com.samebug.clients.idea.services.android;

import com.android.ddmlib.*;
import com.android.tools.idea.logcat.AndroidLogcatReceiver;
import com.android.tools.idea.logcat.AndroidLogcatView;
import com.android.tools.idea.monitor.AndroidToolWindowFactory;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.samebug.clients.common.search.api.LogScannerFactory;
import com.samebug.clients.common.search.api.entities.tracking.DebugSessionInfo;
import com.samebug.clients.idea.components.application.Tracking;
import com.samebug.clients.idea.components.project.SamebugProjectComponent;
import com.samebug.clients.idea.components.project.StackTraceMatcherFactory;
import com.samebug.clients.idea.components.project.ToolWindowController;
import com.samebug.clients.idea.console.ConsoleWatcher;
import com.samebug.clients.idea.processadapters.LogcatWriter;
import com.samebug.clients.idea.tracking.Events;
import com.samebug.clients.idea.util.AndroidSdkUtil;
import org.jetbrains.android.actions.AndroidEnableAdbServiceAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class LogcatProcessWatcher implements LogcatService, AndroidDebugBridge.IDeviceChangeListener {
    final private Project myProject;

    // AbstractProjectComponent overrides
    public LogcatProcessWatcher(Project project) {
        myProject = project;
    }

    @Override
    public void projectOpened() {
        File adb = AndroidSdkUtil.getAdb(myProject);
        if (adb != null) {
            AndroidDebugBridge.initIfNeeded(AndroidEnableAdbServiceAction.isAdbServiceEnabled());
            AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(adb.getPath(), false);
            AndroidDebugBridge.addDeviceChangeListener(this);
            if (bridge.isConnected()) {
                for (IDevice device : bridge.getDevices()) {
                    initReceiver(device);
                }
            }
        }
    }

    @Override
    public void projectClosed() {
        AndroidDebugBridge.removeDeviceChangeListener(this);
        for (Integer deviceHash : listeners.keySet()) {
            removeReceiver(deviceHash);
        }
        listeners.clear();
    }

    // IDeviceChangeListener overrides
    @Override
    public void deviceConnected(IDevice device) {
        initReceiver(device);
    }


    @Override
    public void deviceDisconnected(IDevice device) {
        removeReceiver(device);
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        initReceiver(device);
    }

    // implementation
    private synchronized void initReceiver(@NotNull IDevice device) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                myProject.getComponent(ToolWindowController.class).changeToolwindowIcon(false);
            }
        });
        if (device.isOnline()) {
            Integer deviceHashCode = System.identityHashCode(device);
            if (listeners.get(deviceHashCode) == null) {
                createListener(device, deviceHashCode);
            }
        }
    }

    private AndroidLogcatReceiver createListener(@NotNull final IDevice device, Integer deviceHashCode) {
        final DebugSessionInfo sessionInfo = new DebugSessionInfo("logcat");

        final LogScannerFactory scannerFactory = new StackTraceMatcherFactory(myProject, sessionInfo);
        final AndroidLogcatReceiver receiver = new AndroidLogcatReceiver(device, new LogcatWriter(myProject, scannerFactory.createScanner()));
        listeners.put(deviceHashCode, receiver);
        debugSessionInfos.put(deviceHashCode, sessionInfo);

        ToolWindow t = ToolWindowManager.getInstance(myProject).getToolWindow(AndroidToolWindowFactory.TOOL_WINDOW_ID);
        for (Content content : t.getContentManager().getContents()) {
            final AndroidLogcatView view = content.getUserData(AndroidLogcatView.ANDROID_LOGCAT_VIEW_KEY);

            if (view != null) {
                ConsoleView c = view.getLogConsole().getConsole();
                if (c instanceof ConsoleViewImpl) {
                    // do we have to keep this reference?
                    new ConsoleWatcher((ConsoleViewImpl) c, sessionInfo);
                }
            }
        }

        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                try {
                    device.executeShellCommand("logcat -v long", receiver, 0L, TimeUnit.NANOSECONDS);
                } catch (TimeoutException e) {
                    LOGGER.warn("Unable to create receiver for device " + device.getName(), e);
                    Notifications.Bus.notify(new Notification("samebug", "Adb connection failure",
                            "Unable to create receiver for device " + device.getName(), NotificationType.WARNING));
                } catch (AdbCommandRejectedException e) {
                    LOGGER.warn("Unable to create receiver for device " + device.getName(), e);
                    Notifications.Bus.notify(new Notification("samebug", "Adb connection failure",
                            "Unable to create receiver for device " + device.getName(), NotificationType.WARNING));
                } catch (ShellCommandUnresponsiveException e) {
                    LOGGER.warn("Unable to create receiver for device " + device.getName(), e);
                    Notifications.Bus.notify(new Notification("samebug", "Adb connection failure",
                            "Unable to create receiver for device " + device.getName(), NotificationType.WARNING));
                } catch (IOException e) {
                    LOGGER.warn("Unable to create receiver for device " + device.getName(), e);
                    Notifications.Bus.notify(new Notification("samebug", "Adb connection failure",
                            "Unable to create receiver for device " + device.getName(), NotificationType.WARNING));
                }

            }
        });

        Tracking.projectTracking(myProject).trace(Events.debugStart(myProject, sessionInfo));

        return receiver;
    }

    private synchronized void removeReceiver(@NotNull IDevice device) {
        removeReceiver(System.identityHashCode(device));
    }

    private void removeReceiver(Integer deviceHashCode) {
        AndroidLogcatReceiver receiver = listeners.get(deviceHashCode);
        if (receiver != null) {
            receiver.processNewLine("\n");
            receiver.done();
            Tracking.projectTracking(myProject).trace(Events.debugStop(myProject, debugSessionInfos.get(deviceHashCode)));

            DebugSessionInfo sessionInfo = debugSessionInfos.get(deviceHashCode);
            debugSessionInfos.remove(deviceHashCode);
            listeners.remove(deviceHashCode);
            myProject.getComponent(SamebugProjectComponent.class).getSessionService().removeSession(sessionInfo);
        }
    }

    private final Map<Integer, AndroidLogcatReceiver> listeners = new HashMap<Integer, AndroidLogcatReceiver>();
    private final Map<Integer, DebugSessionInfo> debugSessionInfos = new HashMap<Integer, DebugSessionInfo>();
    private final static Logger LOGGER = Logger.getInstance(LogcatProcessWatcher.class);
}