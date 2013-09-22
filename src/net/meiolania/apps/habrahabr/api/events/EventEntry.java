package net.meiolania.apps.habrahabr.api.events;

import java.util.List;

import net.meiolania.apps.habrahabr.api.hubs.HubEntry;

public class EventEntry {
	private String title;
	private String url;
	private String month;
	private int day;
	private String dayOfWeek;
	private List<HubEntry> hubs;
	private String text;
	private int visitersCount;

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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public List<HubEntry> getHubs() {
		return hubs;
	}

	public void setHubs(List<HubEntry> hubs) {
		this.hubs = hubs;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getVisitersCount() {
		return visitersCount;
	}

	public void setVisitersCount(int visitersCount) {
		this.visitersCount = visitersCount;
	}

}