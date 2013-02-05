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

package net.meiolania.apps.habrahabr.fragments.users.loader;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.UsersData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class UsersLoader extends AsyncTaskLoader<ArrayList<UsersData>> {
    public final static String TAG = UsersLoader.class.getName();
    public final static String DEFAULT_URL = "http://habrahabr.ru/users/";

    private String url;

    public UsersLoader(Context context) {
	super(context);

	url = DEFAULT_URL;
    }

    public UsersLoader(Context context, String url) {
	super(context);

	this.url = url;
    }

    @Override
    public ArrayList<UsersData> loadInBackground() {
	ArrayList<UsersData> data = new ArrayList<UsersData>();

	try {
	    Log.i(TAG, "Loading a page: " + url);

	    Document document = Jsoup.connect(url).get();
	    Elements users = document.select("div.user");

	    for (Element user : users) {
		UsersData usersData = new UsersData();

		Element rating = user.select("div.rating").first();
		Element karma = user.select("div.karma").first();
		Element avatar = user.select("div.avatar > a > img").first();
		Element name = user.select("div.userlogin > div.username > a").first();
		Element lifetime = user.select("div.info > div.lifetime").first();

		usersData.setName(name.text());
		usersData.setUrl(name.attr("abs:href"));
		usersData.setRating(rating.text());
		usersData.setKarma(karma.text());
		usersData.setAvatar(avatar.attr("src"));
		usersData.setLifetime(lifetime.text());

		data.add(usersData);
	    }
	} catch (IOException e) {
	}

	return data;
    }

}