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
package com.samebug.clients.idea.components.application;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.common.search.api.exceptions.SamebugClientException;
import com.samebug.clients.idea.messages.model.RefreshUserStatsListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimedTasks implements RefreshUserStatsListener {
    final static Logger LOGGER = Logger.getInstance(TimedTasks.class);

    @NotNull
    final Timer userStatsRefresher;

    @NotNull
    final MessageBusConnection connection;


    public TimedTasks(@NotNull MessageBusConnection connection) {
        this.connection = connection;
        connection.subscribe(RefreshUserStatsListener.TOPIC, this);

        final int UserStatsRefreshInitialDelayInMs = 10 * 1000;
        final int UserStatsRefreshDelayInMs = 10 * 60 * 1000;
        userStatsRefresher = new Timer(UserStatsRefreshDelayInMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.getApplication().getMessageBus().syncPublisher(RefreshUserStatsListener.TOPIC).requestRefresh();
            }
        });
        userStatsRefresher.setInitialDelay(UserStatsRefreshInitialDelayInMs);
        userStatsRefresher.start();
    }


    @Override
    public void requestRefresh() {
        final IdeaSamebugPlugin plugin = IdeaSamebugPlugin.getInstance();
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                try {
                    plugin.client.getUserStats();
                } catch (SamebugClientException e) {
                    LOGGER.warn("Failed to execute getUserStats", e);
                }
            }
        });
    }
}