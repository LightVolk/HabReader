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

package net.meiolania.apps.habrahabr.fragments.hubs.loader;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.HubsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class HubsLoader extends AsyncTaskLoader<ArrayList<HubsData>> {
	public final static String TAG = HubsLoader.class.getName();
	private String url;
	private static int page;

	public HubsLoader(Context context, String url) {
		super(context);

		this.url = url;
	}

	public static void setPage(int page) {
		HubsLoader.page = page;
	}

	@Override
	public ArrayList<HubsData> loadInBackground() {
		ArrayList<HubsData> data = new ArrayList<HubsData>();

		try {
			String readyUrl = url.replace("%page%", String.valueOf(page));

			Log.i(TAG, "Loading a page: " + readyUrl);

			Document document = Jsoup.connect(readyUrl).get();

			Elements hubs = document.select("div.hub");

			for (Element hub : hubs) {
				HubsData hubsData = new HubsData();

				Element index = hub.select("div.habraindex").first();
				Element title = hub.select("div.title > a").first();
				Element stat = hub.select("div.stat").first();

				hubsData.setTitle(title.text());
				hubsData.setUrl(title.attr("abs:href"));
				hubsData.setStat(stat.text());
				hubsData.setIndex(index.text());

				data.add(hubsData);
			}
		} catch (IOException e) {
		}

		return data;
	}

}