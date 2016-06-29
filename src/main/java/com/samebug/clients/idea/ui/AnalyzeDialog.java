package com.samebug.clients.idea.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.unscramble.AnalyzeStacktraceUtil;
import com.samebug.clients.idea.components.application.ClientService;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.idea.resources.SamebugBundle;
import com.samebug.clients.search.api.StackTraceListener;
import com.samebug.clients.search.api.entities.SearchResults;
import com.samebug.clients.search.api.entities.tracking.DebugSessionInfo;
import com.samebug.clients.search.api.exceptions.SamebugClientException;
import com.samebug.clients.search.matcher.StackTraceMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;

final public class AnalyzeDialog extends DialogWrapper {
    final static Logger LOGGER = Logger.getInstance(AnalyzeDialog.class);
    final Project myProject;
    JPanel panel;
    final JPanel warningPanel;

    AnalyzeStacktraceUtil.StacktraceEditorPanel myEditorPanel;

    public AnalyzeDialog(Project project) {
        super(project);
        myProject = project;
        panel = new JPanel();
        warningPanel = new JPanel();
        setTitle(SamebugBundle.message("samebug.menu.analyze.dialog.title"));
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(SamebugBundle.message("samebug.menu.analyze.dialog.description")), BorderLayout.NORTH);
        myEditorPanel = AnalyzeStacktraceUtil.createEditorPanel(myProject, myProject);
        myEditorPanel.pasteTextFromClipboard();
        panel.add(myEditorPanel, BorderLayout.CENTER);
        panel.add(warningPanel, BorderLayout.SOUTH);
        displayWarningIfNotStackTrace();
        return panel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        // TODO focus on search button
        return myEditorPanel.getEditorComponent();
    }

    // TODO call this on editor input/periodically
    protected void displayWarningIfNotStackTrace() {
        final String trace = myEditorPanel.getText();
        boolean hasStackTrace = new Parser().hasStackTrace(trace);
        warningPanel.removeAll();
        if (!hasStackTrace) {
            JLabel warn = new JLabel(SamebugBundle.message("samebug.menu.analyze.dialog.warn"));
            warningPanel.add(warn);
        }
        panel.revalidate();
        panel.repaint();

    }

    @NotNull
    protected Action[] createActions() {
        return new Action[]{getCancelAction(), new SamebugSearch()};
    }

    final protected class SamebugSearch extends DialogWrapperAction implements DumbAware {

        public SamebugSearch() {
            super(SamebugBundle.message("samebug.menu.analyze.dialog.samebugButton"));
        }
        @Override
        protected void doAction(ActionEvent e) {
            final IdeaSamebugPlugin plugin = IdeaSamebugPlugin.getInstance();
            final ClientService client = plugin.getClient();
            final String trace = myEditorPanel.getText();
            ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SearchResults result = client.searchSolutions(trace);
                        try {
                            int searchId = Integer.parseInt(result.searchId);
                            URL url = plugin.getUrlBuilder().search(searchId);
                            BrowserUtil.browse(url);
                        } catch (java.lang.Exception e1) {
                            LOGGER.warn("Failed to open browser for search " + result.searchId, e1);
                        }
                    } catch (SamebugClientException e1) {
                        LOGGER.warn("Failed to execute search", e1);
                    }
                }
            });
        }
    }

    // TODO using the serious parser, get the typename and message and use them for google search
    final protected class GoogleSearch extends DialogWrapperAction implements DumbAware {

        public GoogleSearch() {
            super(SamebugBundle.message("samebug.menu.analyze.dialog.googleButton"));
        }
        @Override
        protected void doAction(ActionEvent e) {
            final String trace = myEditorPanel.getText();
            try {
                URL url = new URL("https://www.google.hu/search?q=" + trace);
                BrowserUtil.browse(url);
            } catch (MalformedURLException e1) {
                LOGGER.warn("Failed to open browser for google search", e1);
            }
        }
    }

    // TODO get the serious parser
    final protected class Parser implements StackTraceListener {
        final StackTraceMatcher parser;
        boolean found;

        public Parser() {
            this.parser = new StackTraceMatcher(this, null);
        }

        public boolean hasStackTrace(String text) {
            found = false;
            for (String line : text.split("\n")) {
                parser.line(line);
            }
            parser.end();
            return found;
        }

        @Override
        public void stacktraceFound(@javax.annotation.Nullable DebugSessionInfo sessionInfo, String stacktrace) {
            found = true;
        }
    }
}
