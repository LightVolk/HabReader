package net.meiolania.apps.habrahabr.api.qa;

import java.util.List;

import net.meiolania.apps.habrahabr.api.hubs.HubEntry;

public class QaEntry {
	private String title;
	private String url;
	private String author;
	private String authorUrl;
	private String date;
	private String text;
	private String htmlText;
	private List<HubEntry> hubs;
	private Integer rating;
	private Integer viewsCount;
	private Integer favoritesCount;
	private Integer answersCount;

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<HubEntry> getHubs() {
		return hubs;
	}

	public void setHubs(List<HubEntry> hubs) {
		this.hubs = hubs;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getViewsCount() {
		return viewsCount;
	}

	public void setViewsCount(Integer viewsCount) {
		this.viewsCount = viewsCount;
	}

	public Integer getFavoritesCount() {
		return favoritesCount;
	}

	public void setFavoritesCount(Integer favoritesCount) {
		this.favoritesCount = favoritesCount;
	}

	public Integer getAnswersCount() {
		return answersCount;
	}

	public void setAnswersCount(Integer answersCount) {
		this.answersCount = answersCount;
	}

}