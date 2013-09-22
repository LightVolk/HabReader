package net.meiolania.apps.habrahabr.api.companies;

import net.meiolania.apps.habrahabr.api.posts.PostEntry;

public class CompanyEntry {
	private String name;
	private String url;
	private String logo;
	private int subscribers;
	private float index;
	private PostEntry lastPost;

	public float getIndex() {
		return index;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(int subscribers) {
		this.subscribers = subscribers;
	}

	public PostEntry getLastPost() {
		return lastPost;
	}

	public void setLastPost(PostEntry lastPost) {
		this.lastPost = lastPost;
	}

}