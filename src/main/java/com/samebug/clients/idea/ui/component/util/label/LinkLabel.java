package com.samebug.clients.idea.ui.component.util.label;

import com.samebug.clients.idea.ui.ColorUtil;
import com.samebug.clients.idea.ui.FontRegistry;
import com.samebug.clients.idea.ui.component.util.interaction.Colors;
import com.samebug.clients.idea.ui.component.util.interaction.InteractiveComponent;

import javax.swing.*;
import java.awt.*;

public class LinkLabel extends JLabel {
    private Colors[] foregroundColors;
    private InteractiveComponent interactiveComponent;

    public LinkLabel() {
        this(null);
    }

    public LinkLabel(String text) {
        this(text, FontRegistry.AvenirRegular, 16);
    }

    public LinkLabel(String text, String fontName, int fontSize) {
        super(text);
        setForeground(ColorUtil.LinkInteraction);
        setFont(new Font(fontName, Font.PLAIN, fontSize));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        updateUI();
    }

    public void setForeground(Colors[] c) {
        foregroundColors = c;
        super.setForeground(ColorUtil.forCurrentTheme(foregroundColors).normal);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        interactiveComponent = InteractiveComponent.updateForegroundInteraction(interactiveComponent, ColorUtil.forCurrentTheme(foregroundColors), this);
    }
}
