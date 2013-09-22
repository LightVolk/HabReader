package net.meiolania.apps.habrahabr.api.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.hubs.HubEntry;
import net.meiolania.apps.habrahabr.api.utils.NumberUtils;
import net.meiolania.apps.habrahabr.api.utils.UrlUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class EventsApi {
	public final static String TAG = EventsApi.class.getSimpleName();
	private AuthApi authApi;

	public EventsApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public List<EventEntry> getEvents(int page, String url) {
		return parseUrl(url.replace("%page%", String.valueOf(page)));
	}

	public List<EventEntry> getEvents(int page, Section section) {
		String url = UrlUtils.createUrl("events/", section.getUrlPath(), "page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<EventEntry> getFeedEvents(int page) {
		String url = UrlUtils.createUrl("feed/events/page", String.valueOf(page));
		return parseUrl(url);
	}

	private List<EventEntry> parseUrl(String url) {
		try {
			Log.i(TAG, "Loading a page: " + url);

			Document document = null;

			if (authApi.isAuth()) {
				Connection connection = Jsoup.connect(url);
				connection.cookie(AuthApi.AUTH_ID, authApi.getAuthId());
				connection.cookie(AuthApi.SESSION_ID, authApi.getSessionId());

				document = connection.get();
			} else {
				document = Jsoup.connect(url).get();
			}

			return parseDocument(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return new ArrayList<EventEntry>();
	}

	private List<EventEntry> parseDocument(Document document) {
		List<EventEntry> eventEntries = new ArrayList<EventEntry>();

		Elements events = document.select("div.events_list > div.event");

		for (Element event : events) {
			Element title = event.select("h1.title > a").first();
			Elements hubs = event.select("div.hubs > a");
			Element month = event.select("div.date > div.month").first();
			Element day = event.select("div.date > div.day").first();
			Element dayOfWeek = event.select("div.date > div.dayname").first();
			Element visitersCount = event.select("div.users_go > div.count").first();
			Element text = event.select("div.text").first();

			EventEntry entry = new EventEntry();

			entry.setTitle(title.text());
			entry.setUrl(title.attr("abs:href"));
			entry.setMonth(month.text());
			entry.setDay(NumberUtils.parse(day));
			entry.setDayOfWeek(dayOfWeek.text());
			entry.setVisitersCount(NumberUtils.parse(visitersCount));
			entry.setText(text.text());

			List<HubEntry> hubsEntries = new ArrayList<HubEntry>();
			for (Element hub : hubs) {
				HubEntry hubEntry = new HubEntry();

				hubEntry.setTitle(hub.text());
				hubEntry.setUrl(hub.attr("abs:href"));

				hubsEntries.add(hubEntry);
			}
			entry.setHubs(hubsEntries);

			eventEntries.add(entry);
		}

		return eventEntries;
	}

	public enum Section {
		Coming("coming/"), Current("current/"), Past("past/");

		private String urlPath;

		Section(String urlPath) {
			this.urlPath = urlPath;
		}

		public String getUrlPath() {
			return urlPath;
		}
	}

}