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
package com.samebug.clients.common.ui.frame.solution;

import com.samebug.clients.common.ui.component.bugmate.IBugmateList;
import com.samebug.clients.common.ui.component.community.IAskForHelp;
import com.samebug.clients.common.ui.component.helpRequest.IMyHelpRequest;
import com.samebug.clients.common.ui.component.hit.ITipHit;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ITipResultsTab {
    final class Model {
        public final List<ITipHit.Model> tipHits;
        public final IBugmateList.Model bugmateList;
        public final IAskForHelp.Model askForHelp;
        @Nullable
        public final IMyHelpRequest.Model myHelpRequest;

        public Model(Model rhs) {
            this(rhs.tipHits, rhs.bugmateList, rhs.askForHelp, rhs.myHelpRequest);
        }

        public Model(List<ITipHit.Model> tipHits, IBugmateList.Model bugmateList, IAskForHelp.Model askForHelp, @Nullable IMyHelpRequest.Model myHelpRequest) {
            this.tipHits = tipHits;
            this.bugmateList = bugmateList;
            this.askForHelp = askForHelp;
            this.myHelpRequest = myHelpRequest;
        }

        public int getTipsSize() {
            return tipHits.size();
        }
    }
}
