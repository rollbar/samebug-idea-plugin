package com.samebug.clients.swing.ui.component.solutions.requestTip;

import com.samebug.clients.swing.ui.component.util.label.SamebugLabel;
import com.samebug.clients.swing.ui.component.util.multiline.SamebugMultilineLabel;
import com.samebug.clients.swing.ui.component.util.panel.SamebugPanel;
import com.samebug.clients.swing.ui.global.ColorService;
import com.samebug.clients.swing.ui.global.DrawService;
import com.samebug.clients.swing.ui.global.FontService;
import com.samebug.clients.swing.ui.global.MessageService;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO extract common parts with WriteTipArea
public final class WriteRequestArea extends JComponent {
    // TODO is it necessary? anyway, it can still be bypassed by pasting large text
    public static final int MaxOvershootCharacters = 200;
    public static final int MaxCharacters = 140;
    public static final int MinCharacters = 5;
    public static final int DefaultLines = 4;

    final BorderedArea borderedArea;

    final BugmateList bugmateList;

    public WriteRequestArea(BugmateList bugmateList) {
        this.bugmateList = bugmateList;
        borderedArea = new BorderedArea();

        setLayout(new MigLayout("fillx", "0[fill]0", "0[]0"));
        add(borderedArea);
    }

    final class TipRequestConstraints extends DocumentFilter {
        @Override
        public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
            super.replace(fb, offs, length, str, a);
            final int textSize = fb.getDocument().getLength();
            String text = fb.getDocument().getText(0, textSize);
            assert text.length() == textSize;

            if (textSize >= MaxOvershootCharacters) {
                remove(fb, MaxOvershootCharacters, textSize - MaxOvershootCharacters);
            }

        }
    }
    final class EditableArea extends JTextArea {
        private Color[] foregroundColors;
        private Color[] promptColors;
        {
            setRows(DefaultLines);
            setLineWrap(true);
            setWrapStyleWord(true);
            PromptSupport.setPrompt(MessageService.message("samebug.component.bugmate.ask.placeholder"), this);

            setBorder(BorderFactory.createEmptyBorder());
            setOpaque(false);
            // TODO this is not really good, promptColors must be set before setInteractionColors as that will call updateColors...
            promptColors = ColorService.FieldPlaceholder;
            setForeground(ColorService.EmphasizedText);
            setFont(FontService.regular(16));
        }
        public void setForeground(Color[] c) {
            foregroundColors = c;
            updateColors();
        }

        public void updateColors() {
            Color currentColor = ColorService.forCurrentTheme(foregroundColors);
            setCaretColor(currentColor);
            PromptSupport.setForeground(ColorService.forCurrentTheme(promptColors), this);
            super.setForeground(currentColor);
        }

        @Override
        public void updateUI() {
            setUI(new BasicTextAreaUI());
            if (foregroundColors != null) updateColors();
        }
    }
    final class LengthCounter extends SamebugLabel {
        int length;

        {
            setFont(FontService.regular(12));
            setForeground(ColorService.UnemphasizedText);
            updateLength(0);
        }

        public void updateLength(final int length) {
            assert (length >= 0);
            this.length = length;
            setText(String.format("%d", MaxCharacters - length));
        }
    }
    final class ExceptionPreview extends SamebugLabel {
        {
            setOpaque(true);
            setBackground(ColorService.Tip);
            setForeground(ColorService.TipText);
            setText(bugmateList.model.exceptionTitle);
            setFont(FontService.regular(14));
            setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        }
    }
    final class BorderedArea extends SamebugPanel {
        final EditableArea editableArea;
        final LengthCounter lengthCounter;
        final ExceptionPreview exceptionPreview;

        {
            editableArea = new EditableArea();
            lengthCounter = new LengthCounter();
            exceptionPreview = new ExceptionPreview();

            setLayout(new MigLayout("fillx", "10[300]10", "10[]10[]6[]10"));
            add(editableArea, "cell 0 0, wmin 0, growx");
            add(lengthCounter, "cell 0 1, align right");
            add(exceptionPreview, "cell 0 2, wmin 0, growx");

            setBackground(ColorService.Background);


            ((AbstractDocument) editableArea.getDocument()).setDocumentFilter(new TipRequestConstraints());
            editableArea.getDocument().addDocumentListener(new TipEditorListener());
            editableArea.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
            // Make the whole bordered area behave like a text area
            setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    editableArea.grabFocus();
                }
            });
        }

        @Override
        public void paintBorder(Graphics g) {
            Graphics2D g2 = DrawService.init(g);

            Color borderColor;
            // TODO error
            if (editableArea.hasFocus()) borderColor = ColorService.forCurrentTheme(ColorService.FieldFocusBorder);
            else borderColor = ColorService.forCurrentTheme(ColorService.FieldNormalBorder);
            g2.setColor(borderColor);
            g2.drawRect(0,0,getWidth() - 1, getHeight() - 1);
        }


        final class TipEditorListener implements DocumentListener {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lengthCounter.updateLength(e.getDocument().getLength());
                lengthCounter.repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lengthCounter.updateLength(e.getDocument().getLength());
                lengthCounter.repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lengthCounter.updateLength(e.getDocument().getLength());
                lengthCounter.repaint();
            }
        }
    }
}
