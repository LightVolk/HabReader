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

package net.meiolania.apps.habrahabr.fragments.posts.loader;

import java.util.List;

import net.meiolania.apps.habrahabr.api.HabrAuthApi;
import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.api.posts.PostsApi;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PostsLoader extends AsyncTaskLoader<List<PostEntry>> {
	public final static String TAG = PostsLoader.class.getName();
	private static int page;
	private PostsApi postsApi;

	public PostsLoader(Context context, String url) {
		super(context);

		postsApi = new PostsApi(new HabrAuthApi());
	}

	public static void setPage(int page) {
		PostsLoader.page = page;
	}

	@Override
	public List<PostEntry> loadInBackground() {
		return postsApi.getPosts(page);
	}

}