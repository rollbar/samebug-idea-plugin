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
package com.samebug.clients.common.search.api.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public abstract class Search {
    @NotNull
    protected Integer id;
    @NotNull
    protected Date timestamp;
    @Nullable
    protected String visitorId;
    @Nullable
    protected Long userId;
    @Nullable
    protected Long teamId;

    @NotNull
    public Integer getId() {
        return id;
    }

    @NotNull
    public Date getTimestamp() {
        return timestamp;
    }

    @Nullable
    public String getVisitorId() {
        return visitorId;
    }

    @Nullable
    public Long getUserId() {
        return userId;
    }

    @Nullable
    public Long getTeamId() {
        return teamId;
    }
}
