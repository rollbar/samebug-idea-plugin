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
package com.samebug.clients.idea.ui.controller.solution;

import com.samebug.clients.common.api.entities.helpRequest.MyHelpRequest;
import com.samebug.clients.common.api.form.RevokeHelpRequest;
import com.samebug.clients.common.ui.component.helpRequest.IMyHelpRequest;
import com.samebug.clients.idea.ui.controller.form.RevokeHelpRequestFormHandler;

final class RevokeHelpRequestListener implements IMyHelpRequest.Listener {
    final SolutionFrameController controller;

    public RevokeHelpRequestListener(final SolutionFrameController controller) {
        this.controller = controller;
    }

    @Override
    public void revokeHelpRequest(final IMyHelpRequest source, final String helpRequestId) {
        new RevokeHelpRequestFormHandler(controller.view, source, new RevokeHelpRequest(helpRequestId)) {

            @Override
            protected void afterPostForm(MyHelpRequest response) {
                source.successRevoke();
                controller.load();
            }
        }.execute();
    }
}