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
package com.samebug.clients.common.ui.component.profile;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

public interface IProfilePanel {
    @NotNull
    Model getModel();

    void setModel(@NotNull Model model);

    final class Model {
        public final Integer userId;
        public final String name;
        public final URL avatarUrl;
        public final ConnectionStatus status;

        public Model(Model rhs) {
            this(rhs.userId, rhs.name, rhs.avatarUrl, rhs.status);
        }

        public Model(Integer userId, String name, URL avatarUrl, ConnectionStatus status) {
            this.userId = userId;
            this.name = name;
            this.avatarUrl = avatarUrl;
            this.status = status;
        }
    }

    interface Listener {
        void profileClicked(Integer userId);
    }
}
