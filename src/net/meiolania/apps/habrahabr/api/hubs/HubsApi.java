package net.meiolania.apps.habrahabr.api.hubs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.AuthApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class HubsApi {
	public final static String TAG = HubsApi.class.getSimpleName();
	private AuthApi authApi;

	public HubsApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public List<HubEntry> getHubs(String url, int page) {
		return parseUrl(url.replace("%page%", String.valueOf(page)));
	}

	private List<HubEntry> parseUrl(String url) {
		try {
			Log.i(TAG, "Loading a page: " + url);

			Document document = null;
			if (authApi.isAuth()) {
				document = Jsoup.connect(url).cookie(AuthApi.SESSION_ID, authApi.getSessionId())
						.cookie(AuthApi.AUTH_ID, authApi.getAuthId()).get();
			} else {
				document = Jsoup.connect(url).get();
			}

			return parseDocument(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return null;
	}

	public List<HubEntry> parseDocument(Document document) {
		List<HubEntry> hubs = new ArrayList<HubEntry>();

		Elements hubsElements = document.select("div.hubs > div.hub");
		for (Element hub : hubsElements) {
			Element title = hub.select("div.title > a").first();
			Element index = hub.select("div.habraindex").first();
			Element membersCount = hub.select("div.stat > a.members_count").first();

			Elements info = hub.select("div.stat").first().getElementsByTag("a");
			Element postsCount = (info != null ? info.get(0) : null);
			Element qaCount = ((info != null && info.size() > 1) ? info.get(1) : null);

			HubEntry hubEntry = new HubEntry();

			hubEntry.setTitle(title.text());
			hubEntry.setUrl(title.attr("abs:href"));
			hubEntry.setIndex(index.text());

			hubEntry.setMembersCount(membersCount.text());

			if (postsCount != null)
				hubEntry.setPostsCount(postsCount.text());

			if (qaCount != null)
				hubEntry.setQaCount(qaCount.text());

			hubs.add(hubEntry);
		}

		return hubs;
	}

}