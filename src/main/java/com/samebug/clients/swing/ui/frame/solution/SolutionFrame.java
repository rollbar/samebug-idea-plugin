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
package com.samebug.clients.swing.ui.frame.solution;

import com.samebug.clients.common.ui.frame.solution.ISolutionFrame;
import com.samebug.clients.swing.ui.base.frame.BasicFrame;
import com.samebug.clients.swing.ui.base.panel.SamebugPanel;
import com.samebug.clients.swing.ui.component.profile.ProfilePanel;
import com.samebug.clients.swing.ui.modules.ListenerService;

import javax.swing.*;
import java.awt.*;

public final class SolutionFrame extends BasicFrame implements ISolutionFrame {
    private Solutions solutions;

    public SolutionFrame() {
        setLoading();
    }

    public void loadingSucceeded(Model model) {
        solutions = new Solutions(model);
        addMainComponent(solutions);
    }

    private final class Solutions extends SamebugPanel {
        private final JPanel exceptionHeader;
        private final ResultTabs tabs;
        private final JPanel profilePanel;

        Solutions(Model model) {
            exceptionHeader = new SearchHeaderPanel(model.header);
            tabs = new ResultTabs(model.resultTabs);
            profilePanel = new ProfilePanel(model.profilePanel);

            setLayout(new BorderLayout());
            add(exceptionHeader, BorderLayout.NORTH);
            add(tabs, BorderLayout.CENTER);
            add(profilePanel, BorderLayout.SOUTH);
        }
    }


    public Listener getListener() {
        return ListenerService.getListener(this, Listener.class);
    }
}