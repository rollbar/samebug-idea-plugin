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
package com.samebug.clients.http.entities.helprequest;

import com.samebug.clients.http.entities.search.ReadableSearchGroup;
import com.samebug.clients.http.entities.user.SamebugUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public final class HelpRequestMatch {
    private HelpRequest helpRequest;
    private SamebugUser bugmate;
    private Integer workspaceId;
    private ReadableSearchGroup matchingGroup;
    private Date viewedAt;

    @NotNull
    public HelpRequest getHelpRequest() {
        return helpRequest;
    }

    @NotNull
    public SamebugUser getBugmate() {
        return bugmate;
    }

    @Nullable
    public Integer getWorkspaceId() {
        return workspaceId;
    }

    @NotNull
    public ReadableSearchGroup getMatchingGroup() {
        return matchingGroup;
    }

    @Nullable
    public Date getViewedAt() {
        return viewedAt;
    }
}