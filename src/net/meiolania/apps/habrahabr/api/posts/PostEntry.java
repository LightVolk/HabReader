package net.meiolania.apps.habrahabr.api.posts;

import java.util.List;

import net.meiolania.apps.habrahabr.api.hubs.HubsEntry;

public class PostEntry {
	private String title;
	private int id;
	private String url;
	private String author;
	private String authorUrl;
	private String date;
	private List<HubsEntry> hubs;
	private String text;
	private Integer rating;
	private Integer viewCount;
	private Integer favoritesCount;
	private Integer commentsCount;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getId() {
		return id;
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

	public List<HubsEntry> getHubs() {
		return hubs;
	}

	public void setHubs(List<HubsEntry> hubs) {
		this.hubs = hubs;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getFavoritesCount() {
		return favoritesCount;
	}

	public void setFavoritesCount(Integer favoritesCount) {
		this.favoritesCount = favoritesCount;
	}

	public Integer getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}

}