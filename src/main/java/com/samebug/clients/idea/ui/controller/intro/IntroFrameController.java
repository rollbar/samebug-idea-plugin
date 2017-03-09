/**
 * Copyright 2017 Samebug, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.idea.ui.controller.intro;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.samebug.clients.common.ui.frame.IIntroFrame;
import com.samebug.clients.idea.components.project.ToolWindowController;
import com.samebug.clients.idea.ui.controller.ConnectionStatusController;
import com.samebug.clients.swing.ui.frame.intro.IntroFrame;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

// TODO
public class IntroFrameController implements Disposable {
    final static Logger LOGGER = Logger.getInstance(IntroFrameController.class);
    final ToolWindowController twc;
    final Project myProject;
    final ConnectionStatusController connectionStatusController;

    @NotNull
    final IIntroFrame view;

    public IntroFrameController(ToolWindowController twc, Project project) {
        this.twc = twc;
        this.myProject = project;
        this.view = new IntroFrame();

        MessageBus messageBus = myProject.getMessageBus();
        connectionStatusController = new ConnectionStatusController(view, messageBus);
    }

    @NotNull
    public JComponent getControlPanel() {
        return (IntroFrame) view;
    }

    @Override
    public void dispose() {
        connectionStatusController.dispose();
    }
}
