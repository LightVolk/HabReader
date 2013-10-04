package net.meiolania.apps.habrahabr;

import net.meiolania.apps.habrahabr.api.AuthApi;
import android.content.Context;

public final class HabrAuthApi implements AuthApi {
	private static HabrAuthApi instance = new HabrAuthApi();
	private String login;
	private String sessionId;
	private String authId;

	private HabrAuthApi() {

	}

	public static HabrAuthApi getInstance() {
		if (instance == null)
			instance = new HabrAuthApi();
		return instance;
	}

	public void init(Context context) {
		Preferences preferences = Preferences.getInstance(context);

		this.login = preferences.getLogin();
		this.sessionId = preferences.getPHPSessionId();
		this.authId = preferences.getHSecId();
	}

	@Override
	public boolean isAuth() {
		return (sessionId != null && authId != null);
	}

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public String getAuthId() {
		return authId;
	}

	@Override
	public void auth(String login, String sessionId, String authId) {
		this.login = login;
		this.sessionId = sessionId;
		this.authId = authId;
	}

	@Override
	public void logout() {

	}

}