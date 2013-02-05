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

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.data.PostsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PostsLoader extends AsyncTaskLoader<ArrayList<PostsData>> {
    public final static String TAG = PostsLoader.class.getName();
    private String url;
    private boolean postsFullInfo = false;
    private static int page;

    public PostsLoader(Context context, String url) {
	super(context);

	this.url = url;

	this.postsFullInfo = Preferences.getInstance(context).getPostsFullInfo();
    }

    public static void setPage(int page) {
	PostsLoader.page = page;
    }

    @Override
    public ArrayList<PostsData> loadInBackground() {
	ArrayList<PostsData> data = new ArrayList<PostsData>();

	try {
	    String readyUrl = url.replace("%page%", String.valueOf(page));

	    Log.i(TAG, "Loading a page: " + readyUrl);
	    
	    Document document;
	    if(!User.getInstance().isLogged())
		document = Jsoup.connect(readyUrl).get();
	    else
		document = Jsoup.connect(readyUrl)
				.cookie(User.PHPSESSION_ID, User.getInstance().getPhpsessid())
				.cookie(User.HSEC_ID, User.getInstance().getHsecid())
				.get();

	    Elements posts = document.select("div.post");

	    for (Element post : posts) {
		PostsData postsData = new PostsData();

		Element postTitle = post.select("a.post_title").first();
		Element hubs = post.select("div.hubs").first();
		Element date = post.select("div.published").first();
		Element author = post.select("div.author > a").first();
		Element comments = post.select("div.comments > span.all").first();
		
		Element score = post.select("a.score").first();
		if(score == null)
		    score = post.select("span.score").first();

		if (postsFullInfo) {
		    Element postText = post.select("div.content").first();
		    Element image = postText.select("img").first();
		    
		    postsData.setText(postText.text());
		    postsData.setImage(image != null ? image.attr("abs:src") : "");
		}

		postsData.setTitle(postTitle.text());
		postsData.setUrl(postTitle.attr("abs:href"));
		postsData.setHubs(hubs.text());
		postsData.setDate(date.text());
		postsData.setAuthor(author != null ? author.text() : "");
		postsData.setComments(comments != null ? comments.text() : "0");
		postsData.setScore(score.text());

		data.add(postsData);
	    }
	} catch (IOException e) {
	}

	return data;
    }

}
