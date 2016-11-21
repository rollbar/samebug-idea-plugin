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
package com.samebug.clients.idea.console;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.DumbAware;
import com.samebug.clients.common.entities.search.Saved;
import com.samebug.clients.idea.messages.view.FocusListener;
import com.samebug.clients.idea.resources.SamebugIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class SavedSearchMark extends GutterIconRenderer implements DumbAware {
    private final Saved search;

    public SavedSearchMark(Saved search) {
        this.search = search;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return SamebugIcons.gutterSamebug;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SavedSearchMark) {
            SavedSearchMark rhs = (SavedSearchMark) o;
            return rhs.search.equals(search);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return search.hashCode();
    }

    @Override
    public boolean isNavigateAction() {
        return true;
    }

    @Override
    @NotNull
    public String getTooltipText() {
        return "Search " + search.getSavedSearch().getSearchId()
                + "\nClick to show solutions.";
    }

    @NotNull
    public AnAction getClickAction() {
        return new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent e) {
                getEventProject(e).getMessageBus().syncPublisher(FocusListener.TOPIC).focusOnSearch(search.getSavedSearch().getSearchId());
            }
        };
    }

}

