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

package net.meiolania.apps.habrahabr.fragments.events.loader;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.data.EventsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class EventLoader extends AsyncTaskLoader<ArrayList<EventsData>> {
	public final static String TAG = EventLoader.class.getName();
	private String url;
	private static int page;

	public EventLoader(Context context, String url) {
		super(context);

		this.url = url;
	}

	public static void setPage(int page) {
		EventLoader.page = page;
	}

	@Override
	public ArrayList<EventsData> loadInBackground() {
		ArrayList<EventsData> data = new ArrayList<EventsData>();

		try {
			String readyUrl = url.replace("%page%", String.valueOf(page));

			Log.i(TAG, "Loading a page: " + readyUrl);

			Document document;
			if (!User.getInstance().isLogged())
				document = Jsoup.connect(readyUrl).get();
			else
				document = Jsoup.connect(readyUrl).cookie(User.PHPSESSION_ID, User.getInstance().getPhpsessid())
						.cookie(User.HSEC_ID, User.getInstance().getHsecid()).get();

			Elements events = document.select("div.event");

			for (Element event : events) {
				EventsData eventsData = new EventsData();

				Element title = event.select("h1.title > a").first();
				eventsData.setTitle(title.text());
				eventsData.setUrl(title.attr("abs:href"));

				Element text = event.select("div.text").first();
				Element month = event.select("div.date > div.month").first();
				Element day = event.select("div.date > div.day").first();
				Element hubs = event.select("div.hubs").first();

				eventsData.setText(text.text());
				eventsData.setDate(day.text() + " " + month.text());
				eventsData.setHubs(hubs.text());

				data.add(eventsData);
			}
		} catch (IOException e) {
		}

		return data;
	}

}