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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.EventFullData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class EventShowLoader extends AsyncTaskLoader<EventFullData> {
	public final static int INFO_LOCATION = 0;
	public final static int INFO_PAY = 2;
	public final static int INFO_SITE = 3;
	public final static int INFO_DATE = 1;
	private String url;

	public EventShowLoader(Context context, String url) {
		super(context);

		this.url = url;
	}

	@Override
	public EventFullData loadInBackground() {
		EventFullData event = new EventFullData();

		try {
			Document document = Jsoup.connect(url).get();

			Element title = document.select("h1.title").first();
			Element description = document.select("div.description").first();
			Elements additionalInfo = document.select("div.info_block").first()
					.getElementsByTag("dd");

			Element location = null, pay = null, site = null, date = null;
			// TODO: need to test this code more
			int i = 0;
			for (Element info : additionalInfo) {
				switch (i) {
					case INFO_LOCATION:
						location = info;
						break;
					case INFO_DATE:
						date = info;
						break;
					case INFO_PAY:
						pay = info;
						break;
					case INFO_SITE:
						site = info;
						break;
				}
				i++;
			}

			event.setTitle(title.text());
			event.setUrl(url);
			event.setDate(date != null ? date.text() : "");
			event.setText(description != null ? description.html() : "");
			event.setPay(pay != null ? pay.text() : "");
			event.setLocation(location != null ? location.text() : "");
			event.setSite(site != null ? site.text() : "");
		} catch (IOException e) {
		}

		return event;
	}

}