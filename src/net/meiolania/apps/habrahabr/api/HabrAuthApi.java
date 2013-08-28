package net.meiolania.apps.habrahabr.api;

import net.meiolania.apps.habrahabr.Preferences;
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
		
		setLogin(preferences.getLogin());
		setSessionId(preferences.getPHPSessionId());
		setAuthId(preferences.getHSecId());
	}
	
	@Override
	public boolean isAuth() {
		return false;
	}

	@Override
	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public void setAuthId(String authId) {
		this.authId = authId;
	}

	@Override
	public String getAuthId() {
		return authId;
	}

}