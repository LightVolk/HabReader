package net.meiolania.apps.habrahabr.api.conversations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.api.AuthApi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class ConversationsApi {
	public final static String TAG = ConversationsApi.class.getSimpleName();
	private AuthApi authApi;

	public ConversationsApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public List<ConversationEntry> getConversations(int page) {
		if (!authApi.isAuth())
			return null; // TODO: throw an exception?

		String url = "http://habrahabr.ru/conversations/page" + page;
		try {
			Log.i(TAG, "Loading a page: " + url);

			Connection connection = Jsoup.connect(url);
			connection.timeout(10 * 1000);
			connection.cookie(AuthApi.SESSION_ID, authApi.getSessionId());
			connection.cookie(AuthApi.AUTH_ID, authApi.getAuthId());

			Document document = connection.get();

			return parseDocument(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return null;
	}

	public List<ConversationEntry> parseDocument(Document document) {
		List<ConversationEntry> conversationEntries = new ArrayList<ConversationEntry>();

		Elements conversations = document.select("div#conversations > div.conversation");
		for (Element conversation : conversations) {
			Element url = conversation.select("a.conversation_link").first();
			Element authorAvatar = conversation.select("img").first();
			Element authorName = conversation.select("div.info > div.login").first();
			Element date = conversation.select("div.info > div.time").first();
			Element message = conversation.select("div.info > div.message").first();

			ConversationEntry entry = new ConversationEntry();

			entry.setUrl(url.attr("abs:href"));
			entry.setAuthorName(authorName.text());
			entry.setTime(date.text());
			entry.setMessage(message.text());

			if (authorAvatar.attr("src").startsWith("http:"))
				entry.setAuthorAvatar(authorAvatar.attr("src"));
			else
				entry.setAuthorAvatar("http:" + authorAvatar.attr("src"));

			conversationEntries.add(entry);
		}

		return conversationEntries;
	}

}