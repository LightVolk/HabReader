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

package net.meiolania.apps.habrahabr.fragments.qa.loader;

import java.io.IOException;

import net.meiolania.apps.habrahabr.data.QaFullData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class QaShowLoader extends AsyncTaskLoader<QaFullData> {
	public final static String TAG = QaShowLoader.class.getName();
	private String url;

	public QaShowLoader(Context context, String url) {
		super(context);

		this.url = url;
	}

	@Override
	public QaFullData loadInBackground() {
		QaFullData data = new QaFullData();

		try {
			Log.i(TAG, "Loading a page: " + url);

			Document document = Jsoup.connect(url).get();

			Element title = document.select("span.post_title").first();
			Element hubs = document.select("div.hubs").first();
			Element content = document.select("div.content").first();
			Element tags = document.select("ul.tags").first();
			Element date = document.select("div.published").first();
			Element author = document.select("div.author > a").first();
			Element answers = document.select("span#comments_count").first();

			data.setTitle(title.text());
			data.setHubs(hubs.text());
			data.setContent(content.html());
			data.setTags(tags.text());
			data.setDate(date.text());
			data.setAuthor(author.text());
			data.setAnswers(answers.text());
		} catch (IOException e) {
		}

		return data;
	}

}