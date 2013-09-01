package net.meiolania.apps.habrahabr.api;

public class UrlUtils {
	public final static String TAG = UrlUtils.class.getSimpleName();

	public static String createUrl(String... paths) {
		StringBuilder url = new StringBuilder();

		url.append("http://habrahabr.ru/");
		for (int i = 0; i < paths.length; i++) {
			url.append(paths[i]);
		}

		return url.toString();
	}

}