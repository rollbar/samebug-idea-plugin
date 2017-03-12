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
package com.samebug.clients.idea.ui.controller.helpRequest;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.samebug.clients.common.ui.frame.helpRequest.IHelpRequestFrame;
import com.samebug.clients.idea.components.project.ToolWindowController;
import com.samebug.clients.idea.ui.controller.frame.ConnectionStatusController;
import com.samebug.clients.idea.ui.modules.IdeaDataService;
import com.samebug.clients.swing.ui.frame.helpRequest.HelpRequestFrame;
import com.samebug.clients.swing.ui.modules.DataService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class HelpRequestController implements Disposable {
    final static Logger LOGGER = Logger.getInstance(HelpRequestController.class);
    final ToolWindowController twc;
    final Project myProject;
    final IHelpRequestFrame view;

    final ConnectionStatusController connectionStatusController;


    public HelpRequestController(ToolWindowController twc, Project project) {
        this.twc = twc;
        this.myProject = project;
        view = new HelpRequestFrame();
        DataService.putData((HelpRequestFrame) view, IdeaDataService.Project, project);

        MessageBus messageBus = myProject.getMessageBus();
        connectionStatusController = new ConnectionStatusController(view, messageBus);

    }

    public int getHelpRequestId() {
        // TODO
        return 0;
    }


    @NotNull
    public JComponent getControlPanel() {
        return (HelpRequestFrame) view;
    }

    @Override
    public void dispose() {
        connectionStatusController.dispose();
    }

}