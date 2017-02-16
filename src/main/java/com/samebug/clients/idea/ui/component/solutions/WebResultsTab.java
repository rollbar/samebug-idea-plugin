package com.samebug.clients.idea.ui.component.solutions;

import com.intellij.util.messages.MessageBus;
import com.samebug.clients.idea.resources.SamebugBundle;
import com.samebug.clients.idea.ui.ColorUtil;
import com.samebug.clients.idea.ui.DrawUtil;
import com.samebug.clients.idea.ui.component.util.SamebugButton;
import com.samebug.clients.idea.ui.component.util.scrollPane.SamebugScrollPane;
import com.samebug.clients.idea.ui.component.util.panel.Panel;
import com.samebug.clients.idea.ui.component.util.panel.TransparentPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class WebResultsTab extends TransparentPanel {
    private final Model model;
    private final HelpOthersCTA.Model ctaModel;
    private final MessageBus messageBus;

    private final JScrollPane scrollPane;
    private final JPanel contentPanel;
    private final List<WebHit> webHits;

    public WebResultsTab(MessageBus messageBus, Model model, HelpOthersCTA.Model ctaModel) {
        this.model = new Model(model);
        this.ctaModel = new HelpOthersCTA.Model(ctaModel);
        this.messageBus = messageBus;

        webHits = new ArrayList<WebHit>();
        for (int i = 0; i < model.webHits.size(); i++) {
            WebHit.Model m = model.webHits.get(i);
            WebHit hit = new WebHit(messageBus, m);
            webHits.add(hit);
        }

        if (model.getHitsSize() == 0) {
            contentPanel = new EmptyContentPanel();
        } else {
            contentPanel = new ContentPanel();
        }
        scrollPane = new SamebugScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(contentPanel);

        setLayout(new BorderLayout());
        add(scrollPane);
    }

    private final class EmptyContentPanel extends Panel {
        {
            final NoSolutionCTA cta = new NoSolutionCTA(messageBus, ctaModel);
            cta.setTextForSolutions();
            setLayout(new MigLayout("fillx", "20[fill]0", "20[]20"));
            add(cta);
        }
    }

    private final class ContentPanel extends Panel {
        {
            final ListPanel listPanel = new ListPanel();
            final MoreButton more = new MoreButton();

            setLayout(new MigLayout("fillx", "20[]0", "0[]10[]20"));

            add(listPanel, "cell 0 0, growx");
            add(more, "cell 0 1, al center");
        }
    }


    private final class ListPanel extends TransparentPanel {
        {
            // NOTE I intended to use BoxLayout, but somewhy the webHit did not fill the width of the panel
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // webHits is required to be initialized here (the hit views are actually added to the list)
            for (int i = 0; i < webHits.size(); i++) {
                if (i != 0) add(new Separator(), gbc);
                WebHit hit = webHits.get(i);
                add(hit, gbc);
            }
        }
    }

    private final static class Separator extends Panel {
        {
            setPreferredSize(new Dimension(0, 20 + 1 + 20));
            setForeground(ColorUtil.Separator);
            setBackground(ColorUtil.Background);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = DrawUtil.init(g);
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(getForeground());
            g2.drawLine(0, 21, getWidth(), 21);
        }
    }

    private final class MoreButton extends SamebugButton {
        {
            setText(SamebugBundle.message("samebug.component.webResults.more"));
        }
    }

    public static final class Model {
        private final List<WebHit.Model> webHits;

        public Model(Model rhs) {
            this(rhs.webHits);
        }

        public Model(List<WebHit.Model> webHits) {
            this.webHits = webHits;
        }

        public int getHitsSize() {
            return webHits.size();
        }
    }
}
