package net.meiolania.apps.habrahabr.api;

import org.jsoup.nodes.Element;

public class NumberUtils {
	
	public static Integer Parse(Element text) {
		if (text == null)
			return null;
		
		return Parse(text.text());
	}
	
	public static Integer Parse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			
		}
		
		return null;
	}
	
}