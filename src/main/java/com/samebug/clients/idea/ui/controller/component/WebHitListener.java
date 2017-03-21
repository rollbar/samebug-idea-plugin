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
package com.samebug.clients.idea.ui.controller.component;

import com.samebug.clients.common.ui.component.hit.IWebHit;
import com.samebug.clients.idea.ui.modules.BrowserUtil;

import java.net.URL;

public final class WebHitListener implements IWebHit.Listener {
    public WebHitListener() {
    }

    @Override
    public void urlClicked(URL url) {
        BrowserUtil.browse(url);
    }
}
