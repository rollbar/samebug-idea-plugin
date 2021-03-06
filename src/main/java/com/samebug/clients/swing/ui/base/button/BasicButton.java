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
package com.samebug.clients.swing.ui.base.button;

import com.samebug.clients.swing.ui.base.interaction.ForegroundColorChanger;
import com.samebug.clients.swing.ui.base.interaction.InteractionColors;
import com.samebug.clients.swing.ui.modules.ColorService;
import com.samebug.clients.swing.ui.modules.DrawService;

import javax.swing.*;
import java.awt.*;

/**
 * Samebug style button
 * <p>
 * By not extending the swing AbstractButton, we have lost couple of functionality, e.g. tab selection, mnemonics, accessibility.
 * However, atm we don't need those, and the features that we actually need are much easier to implement on a clean JComponent.
 * <p>
 * If it happens that we need those features from AbstractButton, still don't try to extend it, it is simply not suitable for
 * this level of reusability. Simply lift the necessary code.
 * <p>
 * The features this button provides:
 * - samebug-style interaction (separate colors for normal/rollover/pressed states)
 * - intellij-style themes (separate colors for light/dark theme)
 * - arbitrary component as content
 * - samebug-style filled/not filled state
 */
public abstract class BasicButton extends JComponent {
    protected ForegroundColorChanger interactionListener;
    protected InteractionColors interactionColors;
    protected Color[] backgroundColor;
    protected boolean filled;
    protected Color currentChildrenColor;

    public BasicButton(boolean filled) {
        this.filled = filled;

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setOpaque(false);
        addMouseListener(new BasicButtonListener());

        setBackgroundColor(ColorService.Background);
    }

    public void setInteractionColors(InteractionColors c) {
        interactionColors = c;
        if (interactionListener != null) interactionListener.uninstall();
        interactionListener = new ForegroundColorChanger(c, this);
        if (isEnabled()) {
            interactionListener.install();
            updateColors();
        }
    }

    public void setBackgroundColor(Color[] c) {
        backgroundColor = c;
        updateColors();
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
        if (!filled) setChildrenForeground(getForeground());
        else setChildrenForeground(getBackground());
        repaint();
    }

    public boolean isFilled() {
        return filled;
    }

    @Override
    public void setFont(Font f) {
        super.setFont(f);
        setChildrenFont(f);
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        if (!filled) setChildrenForeground(c);
    }

    @Override
    public void setBackground(Color c) {
        super.setBackground(c);
        if (filled) setChildrenForeground(c);
    }

    @Override
    public void setEnabled(boolean enabled) {
        // TODO this should happen only when the property actually changes, would be better to use property change listener?
        if (isEnabled() == enabled) return;
        super.setEnabled(enabled);
        if (interactionListener != null) {
            if (enabled) interactionListener.install();
            else interactionListener.uninstall();
        }
        updateColors();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = DrawService.init(g);
        paintBorder(g2);
        paintContent(g2);
    }

    private void paintBorder(Graphics2D g2) {
        // draw the rounded border
        g2.setColor(getForeground());
        if (filled)
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, DrawService.RoundingDiameter, DrawService.RoundingDiameter);
        else
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, DrawService.RoundingDiameter, DrawService.RoundingDiameter);
    }

    private void paintContent(Graphics2D g2) {
        // let the children paint themselves
        super.paint(g2);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        updateColors();
    }

    // TODO maybe interaction listener should handle the disabled state also?
    private void updateColors() {
        if (isEnabled() && interactionListener != null) setForeground(interactionListener.getColor());
        else if (!isEnabled() && interactionColors != null)
            setForeground(ColorService.forCurrentTheme(interactionColors.disabled));
        setBackground(ColorService.forCurrentTheme(backgroundColor));
    }


    protected void setChildrenForeground(Color foreground) {
        currentChildrenColor = foreground;
        for (Component c : getComponents()) c.setForeground(currentChildrenColor);
    }

    protected void setChildrenFont(Font font) {
        for (Component c : getComponents()) c.setFont(font);
    }
}
