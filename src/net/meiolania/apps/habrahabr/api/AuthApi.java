package net.meiolania.apps.habrahabr.api;

public interface AuthApi {
	public static final String SESSION_ID = "PHPSESSID";
	public static final String AUTH_ID = "hsec_id";
	
	public boolean isAuth();

	public void setLogin(String login);

	public String getLogin();

	public void setSessionId(String sessionId);

	public String getSessionId();

	public void setAuthId(String authId);

	public String getAuthId();
}