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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.samebug.clients.common.api.entities.solution.MarkResponse;
import com.samebug.clients.common.api.exceptions.SamebugClientException;
import com.samebug.clients.common.services.SolutionService;
import com.samebug.clients.common.ui.component.hit.IMarkButton;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.swing.ui.modules.MessageService;

final class MarkButtonListener implements IMarkButton.Listener {
    final static Logger LOGGER = Logger.getInstance(MarkButtonListener.class);
    final SolutionFrameController controller;

    public MarkButtonListener(final SolutionFrameController controller) {
        this.controller = controller;
    }

    @Override
    public void markClicked(final IMarkButton markButton, final Integer solutionId, final Integer markId) {
        // TODO it does not handles non-SamebugExceptions. Extract the handler for this and formHandler
        markButton.setLoading();
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                SolutionService solutionService = IdeaSamebugPlugin.getInstance().solutionService;
                try {
                    final IMarkButton.Model newModel;
                    if (markId == null) {
                        final MarkResponse response = solutionService.postMark(controller.searchId, solutionId);
                        newModel = controller.conversionService.convertMarkResponse(response);
                    } else {
                        final MarkResponse response = solutionService.retractMark(markId);
                        newModel = controller.conversionService.convertRetractedMarkResponse(response);
                    }
                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            markButton.update(newModel);
                        }
                    });
                } catch (SamebugClientException e) {
                    LOGGER.warn("Failed to post mark", e);
                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            markButton.interruptLoading();
                            controller.view.popupError(MessageService.message("samebug.component.mark.error.unhandled"));
                        }
                    });
                }
            }
        });
    }
}