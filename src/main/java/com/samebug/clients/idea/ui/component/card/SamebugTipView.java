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
package com.samebug.clients.idea.ui.component.card;

import com.samebug.clients.common.ui.Colors;
import com.samebug.clients.common.ui.TextUtil;
import com.samebug.clients.idea.resources.SamebugBundle;
import com.samebug.clients.idea.ui.ColorUtil;
import com.samebug.clients.idea.ui.component.*;
import com.samebug.clients.idea.ui.component.organism.BreadcrumbBar;
import com.samebug.clients.idea.ui.component.organism.MarkPanel;
import com.samebug.clients.search.api.entities.BreadCrumb;
import com.samebug.clients.search.api.entities.RestHit;
import com.samebug.clients.search.api.entities.Tip;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

final public class SamebugTipView extends HitView {
    final Model model;

    public final BreadcrumbBar breadcrumbPanel;
    public final TipText tipLabel;
    public final JPanel sourceReferencePanel;
    public final AvatarPanel avatarPanel;
    public final SBButton writeBetter;

    public SamebugTipView(@NotNull Model model) {
        super(model);
        this.model = model;

        final java.util.List<BreadCrumb> searchBreadcrumb = model.getMatchingBreadCrumb();
        final RestHit<Tip> tip = model.getHit();
        breadcrumbPanel = new BreadcrumbBar(searchBreadcrumb);
        tipLabel = new TipText(tip.solution.tip);
        sourceReferencePanel = new TipSourceReferencePanel(tip.solution);
        avatarPanel = new AvatarPanel(tip.solution.author);
        writeBetter = new WriteBetterButton();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.cardSeparator));
        add(new TransparentPanel() {
            {
                setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
                add(breadcrumbPanel, BorderLayout.SOUTH);
                add(new TransparentPanel() {
                    {
                        add(new TransparentPanel() {
                            {
                                setLayout(new GridBagLayout());
                                GridBagConstraints gbc = new GridBagConstraints();
                                add(markPanel, gbc);
                                gbc.gridx = 2;
                                gbc.weightx = 1;
                                add(new TransparentPanel(), gbc);
                                gbc.gridx = 3;
                                gbc.weightx = 0;
                                add(writeBetter, gbc);
                            }
                        }, BorderLayout.SOUTH);
                        add(new TransparentPanel() {
                            {
                                setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
                                add(sourceReferencePanel, BorderLayout.SOUTH);
                                add(new TransparentPanel() {
                                    {
                                        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                                        add(avatarPanel, BorderLayout.WEST);
                                        add(new TransparentPanel() {
                                            {
                                                setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
                                                add(tipLabel, BorderLayout.CENTER);
                                            }
                                        }, BorderLayout.CENTER);
                                    }
                                }, BorderLayout.CENTER);
                            }
                        }, BorderLayout.CENTER);
                    }
                }, BorderLayout.CENTER);
            }
        }, BorderLayout.CENTER);

        setPreferredSize(new Dimension(400, Math.min(getPreferredSize().height, 250)));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.min(getPreferredSize().height, 250)));
    }

    @Override
    public Color getBackground() {
        return ColorUtil.highlightPanel();
    }

    final class TipSourceReferencePanel extends TransparentPanel {
        public TipSourceReferencePanel(@NotNull Tip tip) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            if (tip.via == null) {
                // no source, show only tip timestamp
                add(new JLabel(String.format("%s", TextUtil.prettyTime(tip.createdAt))) {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.unemphasizedText();
                    }
                });
            } else if (tip.via.author == null) {
                // source without author
                add(new JLabel(String.format("%s | via ", TextUtil.prettyTime(tip.createdAt))) {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.unemphasizedText();
                    }
                });
                add(new LinkLabel(tip.via.source.name, tip.via.url) {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.emphasizedText();
                    }
                });
            } else {
                // source with author
                add(new JLabel(String.format("%s | ", TextUtil.prettyTime(tip.createdAt))) {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.unemphasizedText();
                    }
                });
                add(new LinkLabel(tip.via.author.name, tip.via.author.url) {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.emphasizedText();
                    }
                });
                add(new JLabel(" via ") {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.unemphasizedText();
                    }
                });
                add(new LinkLabel(tip.via.source.name, tip.via.url) {
                    @Override
                    public Color getForeground() {
                        return ColorUtil.emphasizedText();
                    }
                });
            }
        }
    }

    final class WriteBetterButton extends SBButton {
        public WriteBetterButton() {
            super(SamebugBundle.message("samebug.tip.cta.better"));
            setHighlighted(true);
        }

        @Override
        public Color getBackground() {
            return Colors.button;
        }

        @Override
        public Color getForeground() {
            return ColorUtil.emphasizedText();
        }
    }

    public interface Model extends MarkPanel.Model {
        @Override
        @NotNull
        RestHit<Tip> getHit();

        @NotNull
        java.util.List<BreadCrumb> getMatchingBreadCrumb();
    }
}