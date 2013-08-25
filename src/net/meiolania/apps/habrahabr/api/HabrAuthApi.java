package net.meiolania.apps.habrahabr.api;

public final class HabrAuthApi implements AuthApi {
	private String login;
	private String sessionId;
	private String authId;
	
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