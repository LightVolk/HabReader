package net.meiolania.apps.habrahabr.api.comments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.utils.NumberUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class CommentsApi {
	public final static String TAG = CommentsApi.class.getSimpleName();
	private AuthApi authApi;

	public CommentsApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public List<CommentEntry> getQaComments(String url) {
		try {
			Log.i(TAG, "Loading comments: " + url);

			Document document = null;
			if (authApi.isAuth()) {
				document = Jsoup.connect(url).cookie(AuthApi.AUTH_ID, authApi.getAuthId())
						.cookie(AuthApi.SESSION_ID, authApi.getSessionId()).get();
			} else {
				document = Jsoup.connect(url).get();
			}

			return parseQaComments(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load comments: " + url + ". Error: " + e.getMessage());
		}

		return null;
	}

	private List<CommentEntry> parseQaComments(Document document) {
		List<CommentEntry> comments = new ArrayList<CommentEntry>();

		Elements answers = document.select("div.answer");

		for (Element answer : answers) {
			Element authorAnswer = answer.select("div.info > a.username").first();
			Element avatarUrlAnswer = answer.select("div.info > a.avatar > img").first();
			Element dateAnswer = answer.select("div.info > time").first();
			Element ratingAnswer = answer.select("span.score").first();
			Element textAnswer = answer.select("div.message").first();

			CommentEntry answerEntry = new CommentEntry();

			answerEntry.setLevel((byte) 0);
			answerEntry.setAuthor(authorAnswer.text());
			answerEntry.setAuthorUrl(authorAnswer.attr("abs:href"));
			
			if (!avatarUrlAnswer.attr("src").startsWith("http:"))
				answerEntry.setAvatarUrl("http:" + avatarUrlAnswer.attr("src"));
			else
				answerEntry.setAvatarUrl(avatarUrlAnswer.attr("src"));
			
			answerEntry.setDate(dateAnswer.text());
			answerEntry.setRating(NumberUtils.parse(ratingAnswer));
			answerEntry.setText(textAnswer.text());
			answerEntry.setHtmlText(textAnswer.html());

			comments.add(answerEntry);

			Elements replies = answer.select("div.comments > div.comment_item");
			for (Element reply : replies) {
				Element authorReply = reply.select("span.info > a").first();
				Element dateReply = reply.select("span.time").first();
				Element textReply = reply.select("span.text").first();

				CommentEntry replyEntry = new CommentEntry();

				replyEntry.setLevel((byte) 1);
				replyEntry.setAuthor(authorReply.text());
				replyEntry.setAuthorUrl(authorReply.attr("abs:href"));
				replyEntry.setDate(dateReply.text());
				replyEntry.setText(textReply.text());
				replyEntry.setHtmlText(textReply.html());

				comments.add(replyEntry);
			}
		}

		return comments;
	}

	public List<CommentEntry> getPostComments(String url) {
		try {
			Log.i(TAG, "Loading comments: " + url);

			Document document = null;
			if (authApi.isAuth()) {
				document = Jsoup.connect(url).cookie(AuthApi.AUTH_ID, authApi.getAuthId())
						.cookie(AuthApi.SESSION_ID, authApi.getSessionId()).get();
			} else {
				document = Jsoup.connect(url).get();
			}

			return parsePostComments(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load comments: " + url + ". Error: " + e.getMessage());
		}

		return null;
	}

	private List<CommentEntry> parsePostComments(Document document) {
		List<CommentEntry> comments = new ArrayList<CommentEntry>();
		Set<String> containedIds = new HashSet<String>();

		Elements commentsElements = document.select("div.comment_item");
		parsePostComments(comments, containedIds, commentsElements, (byte) 0);

		return comments;
	}

	private void parsePostComments(List<CommentEntry> comments, Set<String> containedIds, Elements commentsElemenets, byte level) {
		for (Element comment : commentsElemenets) {
			String commentId = comment.attr("id");
			if (containedIds.contains(commentId))
				continue;
			
			containedIds.add(commentId);
			
			Element author = comment.select("a.username").first();
			Element avatarUrl = comment.select("a.avatar > img").first();
			Element date = comment.select("div.info > time").first();
			Element rating = comment.select("span.score").first();
			Element text = comment.select("div.message").first();

			CommentEntry entry = new CommentEntry();

			entry.setAuthor(author.text());
			entry.setAuthorUrl(author.attr("abs:href"));

			if (!avatarUrl.attr("src").startsWith("http:"))
				entry.setAvatarUrl("http:" + avatarUrl.attr("src"));
			else
				entry.setAvatarUrl(avatarUrl.attr("src"));

			entry.setDate(date.text());
			entry.setRating(NumberUtils.parse(rating));
			entry.setText(text.text());
			entry.setHtmlText(text.html());
			entry.setLevel(level);

			comments.add(entry);

			Elements replyComments = comment.select("div.reply_comments > div.comment_item");
			parsePostComments(comments, containedIds, replyComments, (byte) (level + 1));
		}
	}

}