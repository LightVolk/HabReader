package net.meiolania.apps.habrahabr.api.hubs;

public class HubEntry {
	private String title;
	private String url;
	private String index;
	private String membersCount;
	private String postsCount;
	private String qaCounts;

	public String getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(String membersCount) {
		this.membersCount = membersCount;
	}

	public String getPostsCount() {
		return postsCount;
	}

	public void setPostsCount(String postsCount) {
		this.postsCount = postsCount;
	}

	public String getQaCounts() {
		return qaCounts;
	}

	public void setQaCount(String qaCounts) {
		this.qaCounts = qaCounts;
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}