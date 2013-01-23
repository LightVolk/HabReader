/*
Copyright 2012 Andrey Zaytsev

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
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.data.QaData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class QaLoader extends AsyncTaskLoader<ArrayList<QaData>> {
    public final static String TAG = QaLoader.class.getName();
    private String url;
    private static int page;

    public QaLoader(Context context, String url) {
	super(context);

	this.url = url;
    }

    public static void setPage(int page) {
	QaLoader.page = page;
    }

    @Override
    public ArrayList<QaData> loadInBackground() {
	ArrayList<QaData> data = new ArrayList<QaData>();

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

	    Elements qaList = document.select("div.post");

	    for (Element qa : qaList) {
		QaData qaData = new QaData();

		Element title = qa.select("a.post_title").first();
		Element hubs = qa.select("div.hubs").first();
		Element answers = qa.select("div.informative").first();
		Element date = qa.select("div.published").first();
		Element author = qa.select("div.author > a").first();
		
		Element score = qa.select("a.score").first();
		if(score == null)
		    score = qa.select("span.score").first();

		qaData.setTitle(title.text());
		qaData.setUrl(title.attr("abs:href"));
		qaData.setHubs(hubs.text());
		qaData.setAnswers(answers.text());
		qaData.setDate(date.text());
		qaData.setAuthor(author.text());
		qaData.setScore(score.text());

		data.add(qaData);
	    }
	} catch (IOException e) {
	}

	return data;
    }

}