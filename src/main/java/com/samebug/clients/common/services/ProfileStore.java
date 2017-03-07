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
package com.samebug.clients.common.services;

import com.samebug.clients.common.search.api.entities.UserInfo;
import com.samebug.clients.common.search.api.entities.UserStats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public final class ProfileStore {
    @NotNull
    final AtomicReference<UserInfo> user;

    @NotNull
    final AtomicReference<UserStats> statistics;

    public ProfileStore() {
        this.user = new AtomicReference<UserInfo>();
        this.statistics = new AtomicReference<UserStats>();
    }

    @Nullable
    public UserInfo getUser() {
        return user.get();
    }

    @Nullable
    public UserStats getUserStats() {
        return statistics.get();
    }
}