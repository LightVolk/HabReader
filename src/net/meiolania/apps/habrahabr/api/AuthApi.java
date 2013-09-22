package net.meiolania.apps.habrahabr.api;

public interface AuthApi {
	public static final String SESSION_ID = "PHPSESSID";
	public static final String AUTH_ID = "hsec_id";

	public boolean isAuth();

	public void auth(String login, String sessionId, String authId);
	
	public void logout();

	public String getLogin();

	public String getSessionId();

	public String getAuthId();
}