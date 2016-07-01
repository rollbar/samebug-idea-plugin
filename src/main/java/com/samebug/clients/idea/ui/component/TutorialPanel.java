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
package com.samebug.clients.idea.ui.component;

import com.samebug.clients.common.ui.Colors;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;

public class TutorialPanel extends TransparentPanel {
    public TutorialPanel(@NotNull final String title, @NotNull final String message) {
        add(new JLabel() {
            {
                setText(title);
                HashMap<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
                attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                setFont(getFont().deriveFont(attributes));
                setForeground(Colors.samebugWhite);
            }
        }, BorderLayout.NORTH);
        add(new TransparentPanel() {
            {
                add(new JLabel() {
                    {
                        setText(message);
                        setForeground(Colors.samebugWhite);
                    }
                });
            }
        }, BorderLayout.CENTER);
    }
}