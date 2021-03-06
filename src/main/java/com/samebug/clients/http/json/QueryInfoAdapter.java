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
package com.samebug.clients.http.json;

import com.google.common.collect.ImmutableMap;
import com.samebug.clients.http.entities.search.QueryInfo;
import com.samebug.clients.http.entities.search.StackTraceInfo;
import com.samebug.clients.http.entities.search.TextSearchInfo;

public class QueryInfoAdapter extends AbstractObjectAdapter<QueryInfo> {
    {
        typeClasses = ImmutableMap.<String, Class<? extends QueryInfo>>builder()
                .put("query-info--stack-trace", StackTraceInfo.class)
                .put("query-info--text", TextSearchInfo.class)
                .build();
    }
}
