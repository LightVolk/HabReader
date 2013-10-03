package net.meiolania.apps.habrahabr.api.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.posts.PostEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class UsersApi {
	public final static String TAG = UsersApi.class.getSimpleName();
	private final static String USERS_PAGE = "http://habrahabr.ru/users/";

	public List<UserEntry> getUsers() {
		return parseUrl(USERS_PAGE);
	}
	
	public List<UserEntry> getUsers(String url) {
		return parseUrl(url);
	}

	private List<UserEntry> parseUrl(String url) {
		try {
			Log.i(TAG, "Loading a page: " + url);

			Document document = Jsoup.connect(url).get();

			return parseDocument(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return new ArrayList<UserEntry>();
	}

	private List<UserEntry> parseDocument(Document document) {
		List<UserEntry> userEntries = new ArrayList<UserEntry>();

		Elements users = document.select("div.peoples_list > div.users > div.user");
		for (Element user : users) {
			Element avatar = user.select("div.avatar > a > img").first();
			Element login = user.select("div.username > a").first();
			Element lifeTime = user.select("div.lifetime").first();
			Element lastPost = user.select("div.last_post > a").first();
			Element rating = user.select("div.rating").first();
			Element karma = user.select("div.karma").first();

			UserEntry entry = new UserEntry();

			entry.setLogin(login.text());
			entry.setUrl(login.attr("abs:href"));
			entry.setLifeTime(lifeTime.text());
			entry.setRating(rating.text());
			entry.setKarma(karma.text());

			if (avatar.attr("src").startsWith("http:"))
				entry.setAvatar(avatar.attr("src"));
			else
				entry.setAvatar("http:" + avatar.attr("src"));

			if (lastPost != null) {
				PostEntry postEntry = new PostEntry();
				postEntry.setTitle(lastPost.text());
				postEntry.setUrl(lastPost.attr("abs:href"));

				entry.setLastPost(postEntry);
			}

			userEntries.add(entry);
		}

		return userEntries;
	}

}