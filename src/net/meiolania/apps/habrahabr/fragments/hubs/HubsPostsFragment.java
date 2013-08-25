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

package net.meiolania.apps.habrahabr.fragments.hubs;

import java.util.List;

import net.meiolania.apps.habrahabr.api.HabrAuthApi;
import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.api.posts.PostsApi;
import net.meiolania.apps.habrahabr.fragments.posts.PostsFragment;
import android.os.Bundle;

public class HubsPostsFragment extends PostsFragment {
	public final static String EXTRA_HUB = "hub";
	private String hub;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		hub = getArguments().getString(EXTRA_HUB);
	}

	@Override
	public List<PostEntry> getPosts(int page) {
		PostsApi postsApi = new PostsApi(HabrAuthApi.getInstance());
		return postsApi.getPosts(page, hub);
	}

}