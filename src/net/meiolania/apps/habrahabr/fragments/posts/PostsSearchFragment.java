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

package net.meiolania.apps.habrahabr.fragments.posts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PostsSearchFragment extends AbstractionPostsFragment {
    public final static String URL = "http://habrahabr.ru/search/page%page%/?target_type=posts&order_by=relevance&q=%query%";
    private String query;

    public PostsSearchFragment(String query) {
	this.query = query;
    }

    @Override
    protected String getUrl() {
	try {
	    return URL.replace("%query%", URLEncoder.encode(query, "UTF-8"));
	} catch (UnsupportedEncodingException e) {
	    return URL.replace("%query%", query);
	}
    }

    @Override
    protected int getLoaderId() {
	return 0;
    }

}