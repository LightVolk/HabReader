package net.meiolania.apps.habrahabr.api.qa;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.utils.UrlUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.util.Log;

public class QaApi {
	public final static String TAG = QaApi.class.getSimpleName();
	private AuthApi authApi;

	public QaApi(AuthApi authApi) {
		this.authApi = authApi;
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
				document = Jsoup.connect(url).cookie(AuthApi.AUTH_ID, authApi.getAuthId())
						.cookie(AuthApi.SESSION_ID, authApi.getSessionId()).get();
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
		List<QaEntry> questions = new ArrayList<QaEntry>();

		return questions;
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