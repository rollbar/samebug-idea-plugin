/**
 * Copyright 2016 Samebug, Inc.
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
package com.samebug.clients.idea.ui.layout;

import javax.swing.*;

/**
 * Created by poroszd on 3/3/16.
 * <p/>
 * <p/>
 * Manually modified:
 * - remove mock data from labels and such
 * - ((DefaultCaret) breadcrumbBar.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
 */
public class SearchGroupCard {
    public JPanel controlPanel;
    public JPanel paddingPanel;
    public JPanel infoBar;
    public JPanel breadcrumbPanel;
    public JPanel contentPanel;
    public JEditorPane breadcrumbBar;
    public JEditorPane titleLabel;
    public JLabel timeLabel;
    public JLabel hitsLabel;
    public JLabel messageLabel;


}