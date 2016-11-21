/**
 * Copyright 2016 Samebug, Inc.
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
package com.samebug.clients.idea.components.project;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.idea.services.SessionService;

public class SamebugProjectComponent extends AbstractProjectComponent {
    private final SessionService sessionService;

    public SamebugProjectComponent(Project project) {
        super(project);
        this.sessionService = new SessionService(project);

        MessageBusConnection messageBusConnection = myProject.getMessageBus().connect(project);
    }

    public SessionService getSessionService() {
        return sessionService;
    }

}
