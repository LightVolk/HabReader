/*
Copyright 2012-2013 Andrey Zaytsev, Sergey Ivanov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package net.meiolania.apps.habrahabr.fragments.feed;

import net.meiolania.apps.habrahabr.fragments.events.AbstractionEventsFragment;

public class FeedEventsFragment extends AbstractionEventsFragment {
    public static final String URL = "http://habrahabr.ru/feed/events/coming/page%page%/";
    
    @Override
    protected String getUrl() {
	return URL;
    }

    @Override
    protected int getLoaderId() {
	return 2;
    }

}