/*
 * Copyright 2017 Samebug, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.idea.ui.controller.toolwindow;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.impl.NotificationsConfigurationImpl;
import com.samebug.clients.http.entities.notification.IncomingHelpRequest;
import com.samebug.clients.idea.notifications.IncomingHelpRequestNotification;

public final class IncomingHelpRequestPopupListener implements com.samebug.clients.idea.messages.IncomingHelpRequest {
    final ToolWindowController twc;

    public IncomingHelpRequestPopupListener(ToolWindowController twc) {
        this.twc = twc;
    }

    @Override
    public void showHelpRequest(IncomingHelpRequest helpRequest) {
        IncomingHelpRequestNotification n = new IncomingHelpRequestNotification(helpRequest);
        NotificationDisplayType notificationType = NotificationsConfigurationImpl.getSettings(n.getGroupId()).getDisplayType();
        if (NotificationDisplayType.BALLOON == notificationType) {
            // This is the type we set by default.
            // In this case, do not use it as a notification, but create instead a custom balloon and show that, because we cannot customize the presentation of a notification
            twc.helpRequestPopupController.showIncomingHelpRequest(helpRequest, n);
        } else {
            // if the user changed it, than handle it as a well-behaved notification
            n.notify(twc.project);
        }
    }

    @Override
    public void addHelpRequest(IncomingHelpRequest helpRequest) {
        // nothing to do
    }
}
