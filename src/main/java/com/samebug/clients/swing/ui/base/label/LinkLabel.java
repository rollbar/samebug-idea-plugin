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
package com.samebug.clients.swing.ui.base.label;

import com.samebug.clients.swing.ui.base.interaction.ForegroundColorChanger;
import com.samebug.clients.swing.ui.base.interaction.InteractionColors;
import com.samebug.clients.swing.ui.modules.ColorService;
import com.samebug.clients.swing.ui.modules.FontService;

import javax.swing.*;
import java.awt.*;

public class LinkLabel extends JLabel {
    private ForegroundColorChanger interactionListener;

    public LinkLabel() {
        this(null);
    }

    public LinkLabel(String text) {
        this(text, FontService.regular(16));
    }

    public LinkLabel(String text, Font font) {
        super(text);
        setInteractionColors(ColorService.LinkInteraction);
        setFont(font);
    }

    public void setInteractionColors(InteractionColors c) {
        if (interactionListener != null) interactionListener.uninstall();
        interactionListener = ForegroundColorChanger.installForegroundInteraction(c, this);
        updateColors();
    }

    @Override
    public void updateUI() {
        setUI(new SamebugLabelUI());
        updateColors();
    }

    private void updateColors() {
        if (interactionListener != null) super.setForeground(interactionListener.getColor());
    }
}
