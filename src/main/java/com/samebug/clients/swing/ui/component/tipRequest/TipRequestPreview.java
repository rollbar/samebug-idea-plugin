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
package com.samebug.clients.swing.ui.component.tipRequest;

import com.samebug.clients.common.ui.component.tipRequest.ITipRequestPreview;
import com.samebug.clients.common.ui.modules.TextService;
import com.samebug.clients.swing.ui.base.label.SamebugLabel;
import com.samebug.clients.swing.ui.base.panel.RoundedBackgroundPanel;
import com.samebug.clients.swing.ui.component.profile.AvatarIcon;
import com.samebug.clients.swing.ui.modules.ColorService;
import com.samebug.clients.swing.ui.modules.FontService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.Date;

public final class TipRequestPreview extends RoundedBackgroundPanel implements ITipRequestPreview {
    private final static int AvatarSize = 40;

    public TipRequestPreview(Model model) {
        setBackgroundColor(ColorService.Tip);
        final AvatarIcon avatar = new AvatarIcon(model.avatarUrl, AvatarSize);
        final SamebugLabel diplayName = new DisplayName(model.displayName);
        final SamebugLabel tipRequestBody = new TipRequestBody(model.tipRequestBody);
        final InfoBar infos = new InfoBar(model.createdAt);
        final SamebugLabel exceptionBody = new ExceptionPreview(model.exceptionBody);

        setLayout(new MigLayout("fillx", MessageFormat.format("20[{0}!]10[250, fill]20", AvatarSize), "20[]0[]15[]20"));

        add(avatar, "cell 0 0, spany 2");
        add(diplayName, "cell 1 0");
        add(infos, "cell 1 0, top left");
        add(tipRequestBody, "cell 1 1, wmin 0");
        add(exceptionBody, "cell 1 2, wmin 0");

    }

    final class DisplayName extends SamebugLabel {
        public DisplayName(String name) {
            super(name);
            setFont(FontService.demi(16));
            setForegroundColor(ColorService.TipText);
        }
    }

    final class TipRequestBody extends SamebugLabel {
        public TipRequestBody(String body) {
            super(body);
            // TODO demi if unread
            setFont(FontService.regular(16));
            setForegroundColor(ColorService.TipText);
        }
    }

    final class InfoBar extends SamebugLabel {
        public InfoBar(Date createdAt) {
            super(TextService.adaptiveTimestamp(createdAt));
            setHorizontalAlignment(SwingConstants.RIGHT);
            setFont(FontService.regular(12));
            setForegroundColor(ColorService.TipText);
        }
    }

    final class ExceptionPreview extends SamebugLabel {
        public ExceptionPreview(String body) {
            super(body);
            setFont(FontService.regular(16));
            // TODO demi if unread
            setOpaque(true);
            setForegroundColor(ColorService.ExceptionPreviewText);
            setBackgroundColor(ColorService.ExceptionPreviewBackground);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
    }
}
