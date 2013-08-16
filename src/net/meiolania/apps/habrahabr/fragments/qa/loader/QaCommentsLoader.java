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

package net.meiolania.apps.habrahabr.fragments.qa.loader;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.CommentsData;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class QaCommentsLoader extends AsyncTaskLoader<ArrayList<CommentsData>> {
	private String url;

	public QaCommentsLoader(Context context, String url) {
		super(context);

		this.url = url;
	}

	@Override
	public ArrayList<CommentsData> loadInBackground() {
		ArrayList<CommentsData> data = new ArrayList<CommentsData>();

		try {
			Document document = Jsoup.connect(url).get();

			Elements answers = document.select("div.answer");

			for (Element answer : answers) {
				CommentsData commentsData = new CommentsData();

				Element name = answer.select("a.username").first();
				Element message = answer.select("div.message").first();
				Element linkToComment = answer.select("a.link_to_comment")
						.first();
				Element score = answer.select("span.score").first();

				commentsData.setUrl(linkToComment.attr("abs:href"));
				commentsData.setAuthor(name.text());
				commentsData.setAuthorUrl(name.attr("abs:href"));
				commentsData.setComment(message.text());
				commentsData.setLevel(0);
				commentsData.setScore(score.text());

				data.add(commentsData);

				Elements comments = answer.select("div.comment_item");

				for (Element comment : comments) {
					commentsData = new CommentsData();

					name = comment.select("span.info > a").first();
					message = comment.select("span.text").first();

					commentsData.setUrl(linkToComment.attr("abs:href"));
					commentsData.setAuthorUrl(name.attr("abs:href"));
					commentsData.setAuthor(name.text());
					commentsData.setComment(message.text());
					commentsData.setLevel(1);
					commentsData.setScore("—");

					data.add(commentsData);
				}
			}
		} catch (IOException e) {
		}

		return data;
	}

}