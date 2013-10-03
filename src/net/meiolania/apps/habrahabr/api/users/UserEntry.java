package net.meiolania.apps.habrahabr.api.users;

import net.meiolania.apps.habrahabr.api.posts.PostEntry;

public class UserEntry {
	private String login;
	private String avatar;
	private String url;
	private String karma;
	private String rating;
	private String lifeTime;
	private PostEntry lastPost;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getKarma() {
		return karma;
	}

	public void setKarma(String karma) {
		this.karma = karma;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(String lifeTime) {
		this.lifeTime = lifeTime;
	}

	public PostEntry getLastPost() {
		return lastPost;
	}

	public void setLastPost(PostEntry lastPost) {
		this.lastPost = lastPost;
	}

}