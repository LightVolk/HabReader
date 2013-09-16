package net.meiolania.apps.habrahabr.api.qa;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class QaApi {
	public final static String TAG = QaApi.class.getSimpleName();
	private AuthApi authApi;

	public QaApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public QaEntry getQuestion(String url) {
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
			
			Element content = document.select("div.post").first();
			
			Element title = content.select("h1.title > span.post_title").first();
			Element author = content.select("div.author > a").first();
			Element date = content.select("div.published").first();
			Elements hubs = content.select("div.hubs > a");
			Element text = content.select("div.content").first();
			Element rating = content.select("div.mark > a.score").first();
			Element pageViews = content.select("div.pageviews").first();
			Element favoritesCount = content.select("div.favs_count").first();
			
			QaEntry qaEntry = new QaEntry();
			
			qaEntry.setTitle(title.text());
			qaEntry.setAuthor(author.text());
			qaEntry.setAuthorUrl(author.attr("abs:href"));
			qaEntry.setDate(date.text());
			qaEntry.setText(text.text());
			qaEntry.setHtmlText(text.html());
			qaEntry.setRating(NumberUtils.parse(rating));
			qaEntry.setViewsCount(NumberUtils.parse(pageViews));
			qaEntry.setFavoritesCount(NumberUtils.parse(favoritesCount));
			
			List<HubEntry> hubsEntries = new ArrayList<HubEntry>();
			for (Element hub : hubs) {
				HubEntry hubEntry = new HubEntry();
				
				hubEntry.setTitle(hub.text());
				hubEntry.setUrl(hub.attr("abs:href"));
				
				hubsEntries.add(hubEntry);
			}
			qaEntry.setHubs(hubsEntries);
			
			return qaEntry;
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return null;
	}

	public List<QaEntry> getQuestions(int page, Section section) {
		String url = UrlUtils.createUrl("qa/", section.getUrlPath(), "page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<QaEntry> searchQuestions(int page, String query) {
		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		String url = UrlUtils.createUrl("search/page", String.valueOf(page), "/", "?target_type=qa&order_by=relevance&q=", query);
		return parseUrl(url);
	}

	public List<QaEntry> getFavoritesQuestions(int page, String userName) {
		String url = UrlUtils.createUrl("users/", userName, "/favorites/questions/page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<QaEntry> getFavoritesQuestions(int page) {
		return getFavoritesQuestions(page, authApi.getLogin());
	}

	public List<QaEntry> getFeedQuestions(int page) {
		String url = UrlUtils.createUrl("feed/qa/page", String.valueOf(page));
		return parseUrl(url);
	}

	private List<QaEntry> parseUrl(String url) {
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

		return new ArrayList<QaEntry>();
	}

	private List<QaEntry> parseDocument(Document document) {
		List<QaEntry> questionsEntries = new ArrayList<QaEntry>();

		Elements questions = document.select("div.post");
		for (Element question : questions) {
			Element title = question.select("h1.title > a").first();
			Element author = question.select("div.author > a").first();
			Element date = question.select("div.published").first();
			Elements hubs = question.select("div.hubs > a");
			Element rating = question.select("div.voting > div.mark").first();
			Element viewsCount = question.select("div.pageviews").first();
			Element favoritesCount = question.select("div.favs_count").first();
			Element answersCount = question.select("div.informative > a").first();

			QaEntry entry = new QaEntry();
			entry.setTitle(title.text());
			entry.setUrl(title.attr("abs:href"));
			entry.setAuthor(author.text());
			entry.setAuthorUrl(author.attr("abs:href"));
			entry.setDate(date.text());
			entry.setRating(NumberUtils.parse(rating));
			entry.setViewsCount(NumberUtils.parse(viewsCount));
			entry.setFavoritesCount(NumberUtils.parse(favoritesCount));
			entry.setAnswersCount(NumberUtils.parse(answersCount));

			List<HubEntry> hubsEntries = new ArrayList<HubEntry>();
			for (Element hub : hubs) {
				HubEntry hubEntry = new HubEntry();

				hubEntry.setTitle(hub.text());
				hubEntry.setUrl(hub.attr("abs:href"));

				hubsEntries.add(hubEntry);
			}
			entry.setHubs(hubsEntries);

			questionsEntries.add(entry);
		}

		return questionsEntries;
	}

	public enum Section {
		Inbox(""), Hot("hot/"), Unanswered("unanswered/");

		private String urlPath;

		Section(String urlPath) {
			this.urlPath = urlPath;
		}

		public String getUrlPath() {
			return urlPath;
		}
	}

}