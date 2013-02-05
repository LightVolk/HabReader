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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.meiolania.apps.habrahabr.data.UsersFullData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class UsersShowLoader extends AsyncTaskLoader<UsersFullData> {
    public final static String TAG = UsersShowLoader.class.getName();
    private String url;

    public UsersShowLoader(Context context, String url) {
	super(context);

	this.url = url;
    }

    @Override
    public UsersFullData loadInBackground() {
	UsersFullData data = new UsersFullData();

	try {
	    Log.i(TAG, "Loading a page: " + url);

	    Document document = Jsoup.connect(url).get();

	    Element avatar = document.select("a.avatar > img").first();
	    Element karma = document.select("div.karma > div.score > div.num").first();
	    Element username = document.select("h2.username > a").first();
	    Element rating = document.select("div.rating > div.num").first();
	    Element birthday = document.select("dd.bday").first();
	    Element fullname = document.select("div.fullname").first();
	    Element summary = document.select("dd.summary").first();
	    Element interests = document.select("dl.interests > dd").first();

	    data.setAvatar(avatar.attr("src"));
	    data.setKarma(karma.text());
	    data.setUsername(username.text());
	    data.setRating(rating.text());
	    data.setBirthday(birthday != null ? birthday.text() : "");
	    data.setFullname(fullname != null ? fullname.text() : "");
	    data.setSummary(summary != null ? summary.html() : "");
	    data.setInterests(interests != null ? interests.text() : "");
	} catch (IOException e) {
	}

	return data;
    }

}