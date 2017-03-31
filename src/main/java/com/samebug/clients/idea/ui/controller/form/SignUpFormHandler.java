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
package com.samebug.clients.idea.ui.controller.form;

import com.samebug.clients.common.api.client.RestError;
import com.samebug.clients.common.api.entities.profile.LoggedInUser;
import com.samebug.clients.common.api.exceptions.SamebugClientException;
import com.samebug.clients.common.api.form.FieldError;
import com.samebug.clients.common.api.form.SignUp;
import com.samebug.clients.common.services.AuthenticationService;
import com.samebug.clients.common.ui.component.authentication.ISignUpForm;
import com.samebug.clients.common.ui.component.form.FormMismatchException;
import com.samebug.clients.common.ui.frame.IFrame;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.swing.ui.modules.MessageService;

import java.util.List;

public abstract class SignUpFormHandler extends PostFormHandler<LoggedInUser> {
    final IFrame frame;
    final ISignUpForm form;
    final SignUp data;

    public SignUpFormHandler(IFrame frame, ISignUpForm form, SignUp data) {
        this.frame = frame;
        this.form = form;
        this.data = data;
    }

    @Override
    protected void beforePostForm() {
        form.startPost();
    }

    @Override
    protected LoggedInUser postForm() throws SamebugClientException {
        final AuthenticationService authenticationService = IdeaSamebugPlugin.getInstance().authenticationService;
        return authenticationService.signUp(data);
    }

    @Override
    protected void handleFieldError(FieldError fieldError, List<String> globalErrors, List<FieldError> fieldErrors) {
        super.handleFieldError(fieldError, globalErrors, fieldErrors);
        if (fieldError.key.equals(SignUp.EMAIL)) fieldErrors.add(fieldError);
        else if (fieldError.key.equals((SignUp.PASSWORD))) fieldErrors.add(fieldError);
        else if (fieldError.key.equals((SignUp.DISPLAY_NAME))) fieldErrors.add(fieldError);
        else globalErrors.add(MessageService.message("samebug.error.pluginBug"));
    }

    @Override
    protected void handleNonFormBadRequests(RestError nonFormError, List<String> globalErrors, List<FieldError> fieldErrors) {
        super.handleNonFormBadRequests(nonFormError, globalErrors, fieldErrors);
        globalErrors.add(MessageService.message("samebug.component.authentication.signUp.error.badRequest"));
    }

    @Override
    protected void handleOtherClientExceptions(SamebugClientException exception, List<String> globalErrors, List<FieldError> fieldErrors) {
        super.handleOtherClientExceptions(exception, globalErrors, fieldErrors);
        globalErrors.add(MessageService.message("samebug.component.authentication.signUp.error.unhandled"));
    }

    @Override
    protected void showFieldErrors(List<FieldError> fieldErrors) throws FormMismatchException {
        form.failPost(fieldErrors);
    }

    @Override
    protected void showGlobalErrors(List<String> globalErrors) {
        if (!globalErrors.isEmpty()) frame.popupError(globalErrors.get(0));
    }
}
