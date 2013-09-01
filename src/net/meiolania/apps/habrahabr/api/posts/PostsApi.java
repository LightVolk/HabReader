package net.meiolania.apps.habrahabr.api.posts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.hubs.HubEntry;
import net.meiolania.apps.habrahabr.api.utils.NumberUtils;
import net.meiolania.apps.habrahabr.api.utils.UrlUtils;

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

	public PostEntry getPost(int id) {
		String url = UrlUtils.createUrl("post/", String.valueOf(id));
		return getPost(url);
	}

	public PostEntry getPost(String url) {
		try {
			Log.i(TAG, "Loading a page: " + url);

			Document document = null;
			if (authApi.isAuth()) {
				document = Jsoup.connect(url).cookie(AuthApi.SESSION_ID, authApi.getSessionId())
						.cookie(AuthApi.AUTH_ID, authApi.getAuthId()).get();
			} else {
				document = Jsoup.connect(url).get();
			}

			Element content = document.select("div.content_left").first();

			Element title = content.select("span.post_title").first();
			Element date = content.select("div.published").first();
			Elements hubs = content.select("div.hubs > a");
			Element text = content.select("div.content").first();
			Element rating = content.select("div.mark > a.score").first();
			Element viewCount = content.select("div.pageviews").first();
			Element favoritesCount = content.select("div.favs_count").first();
			Element author = content.select("div.author > a").first();
			Element commentsCount = document.select("div.comments_list > h2.title > span.comments_count").first();

			PostEntry entry = new PostEntry();

			entry.setTitle(title.text());
			entry.setUrl(url);
			entry.setDate(date.text());
			entry.setAuthor(author.text());
			entry.setAuthorUrl(author.attr("abs:href"));

			List<HubEntry> hubsEntries = new ArrayList<HubEntry>();
			for (Element hub : hubs) {
				HubEntry hubsEntry = new HubEntry();

				hubsEntry.setTitle(hub.text());
				hubsEntry.setUrl(hub.attr("abs:href"));

				hubsEntries.add(hubsEntry);
			}
			entry.setHubs(hubsEntries);

			entry.setText(text.html());

			entry.setRating(NumberUtils.Parse(rating));
			entry.setViewCount(NumberUtils.Parse(viewCount));
			entry.setFavoritesCount(NumberUtils.Parse(favoritesCount));
			entry.setCommentsCount(NumberUtils.Parse(commentsCount));
			
			return entry;
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return null;
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

	public List<PostEntry> getHubsPosts(int page, String hub) {
		String url = UrlUtils.createUrl("hub/", hub, "posts/page", String.valueOf(page));
		return parseUrl(url);
	}
	
	public List<PostEntry> getPosts(int page, String url) {
		return parseUrl(url.replace("%page%", String.valueOf(page)));
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
			Element commentsCount = post.select("div.comments > a > span.all").first();

			Element rating = post.select("a.score").first();
			if (rating == null)
				rating = post.select("span.score").first();

			Element shortText = post.select("div.content").first();

			entry.setTitle(title.text());
			entry.setUrl(title.attr("abs:href"));

			List<HubEntry> hubsEntries = new ArrayList<HubEntry>();
			for (Element hub : hubs) {
				HubEntry hubsEntry = new HubEntry();

				hubsEntry.setTitle(hub.text());
				hubsEntry.setUrl(hub.attr("abs:href"));

				hubsEntries.add(hubsEntry);
			}
			entry.setHubs(hubsEntries);

			entry.setDate(date.text());

			if (author != null) {
				entry.setAuthor(author.text());
				entry.setAuthorUrl(author.attr("abs:href"));
			}

			try {
				entry.setViewCount(Integer.parseInt(viewCount.text()));
				entry.setFavoritesCount(Integer.parseInt(favoritesCount.text()));
				entry.setCommentsCount(Integer.parseInt(commentsCount.text()));
			} catch (NumberFormatException e) {

			}

			entry.setText(shortText.text());

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