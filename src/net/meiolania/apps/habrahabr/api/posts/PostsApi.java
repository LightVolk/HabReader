package net.meiolania.apps.habrahabr.api.posts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.UrlUtils;
import net.meiolania.apps.habrahabr.api.hubs.HubsEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class PostsApi {
	public final static String TAG = PostsApi.class.getSimpleName();
	private AuthApi authApi;

	public PostsApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public List<PostEntry> getPosts(int page) {
		return getPosts(page, Section.Posts, Category.Collective);
	}

	public List<PostEntry> getFavoritesPosts(int page, String userName) {
		String url = UrlUtils.createUrl("users/", userName, "/favorites/page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<PostEntry> getFavoritesPosts(int page) {
		return getFavoritesPosts(page, authApi.getLogin());
	}

	public List<PostEntry> getPosts(int page, String hub) {
		String url = UrlUtils.createUrl("hub/", hub, "posts/page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<PostEntry> getPosts(int page, Section section) {
		String url = UrlUtils.createUrl(section.getUrlPath(), "page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<PostEntry> getPosts(int page, Section section, Category category) {
		String url = UrlUtils.createUrl(section.getUrlPath(), category.getUrlPath(), "page", String.valueOf(page));
		return parseUrl(url);
	}

	public List<PostEntry> searchPosts(int page, String query) {
		String url = UrlUtils.createUrl("search/page", String.valueOf(page), "/", "?target_type=posts&order_by=relevance&q=", query);
		return parseUrl(url);
	}

	private List<PostEntry> parseUrl(String url) {
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

		return new ArrayList<PostEntry>();
	}

	private List<PostEntry> parseDocument(Document document) {
		List<PostEntry> postsEntries = new ArrayList<PostEntry>();

		Elements posts = document.select("div.post");

		for (Element post : posts) {
			PostEntry entry = new PostEntry();

			Element title = post.select("a.post_title").first();
			Elements hubs = post.select("div.hubs > a");
			Element date = post.select("div.published").first();
			Element author = post.select("div.author > a").first();
			Element viewCount = post.select("div.pageviews").first();
			Element favoritesCount = post.select("div.favs_count").first();
			Element commentsCount = post.select("div.comments > span.all").first();

			Element rating = post.select("a.score").first();
			if (rating == null)
				rating = post.select("span.score").first();

			Element shortText = post.select("div.content").first();

			entry.setTitle(title.text());
			entry.setUrl(title.attr("abs:href"));

			List<HubsEntry> hubsEntries = new ArrayList<HubsEntry>();
			for (Element hub : hubs) {
				HubsEntry hubsEntry = new HubsEntry();

				hubsEntry.setTitle(hub.text());
				hubsEntry.setUrl(hub.attr("abs:href"));

				hubsEntries.add(hubsEntry);
			}
			entry.setHubs(hubsEntries);

			entry.setDate(date.text());
			entry.setAuthor(author.text());
			entry.setAuthorUrl(author.attr("abs:href"));
			entry.setViewCount(Integer.parseInt(viewCount.text()));
			entry.setFavoritesCount(Integer.parseInt(favoritesCount.text()));

			if (commentsCount != null)
				entry.setCommentsCount(Integer.parseInt(commentsCount.text()));

			entry.setShortText(shortText.text());

			postsEntries.add(entry);
		}

		return postsEntries;
	}

	public enum Section {
		Feed("feed/"), Posts("posts/");

		private String urlPath;

		Section(String urlPath) {
			this.urlPath = urlPath;
		}

		public String getUrlPath() {
			return urlPath;
		}
	};

	public enum Category {
		Top("top/"), Collective("collective/"), Corporative("corporative/");

		private String urlPath;

		Category(String urlPath) {
			this.urlPath = urlPath;
		}

		public String getUrlPath() {
			return urlPath;
		}
	};
}