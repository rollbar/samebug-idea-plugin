/*
 * Copyright 2018 Samebug, Inc.
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
package com.samebug.clients.common.services;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public final class WebUriBuilder {
    @NotNull
    final URI serverRoot;

    public WebUriBuilder(@NotNull final String serverRoot) {
        assert !serverRoot.endsWith("/");
        this.serverRoot = URI.create(serverRoot);
    }

    @NotNull
    public URI getServerRoot() {
        return serverRoot;
    }

    @NotNull
    public URI search(@NotNull final Integer searchId) {
        return resolveToRoot("/searches/" + searchId);
    }

    @NotNull
    public URI profile(@NotNull final int userId) {
        return resolveToRoot("/user/" + userId);
    }

    @NotNull
    public URI forgottenPassword() {
        return resolveToRoot("/password-forgot");
    }

    @NotNull
    URI resolveToRoot(@NotNull final String uri) {
        return serverRoot.resolve(uri);
    }
}
