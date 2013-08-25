package net.meiolania.apps.habrahabr;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
	public static Typeface ROBOTO_BOLD;
	public static Typeface ROBOTO_REGULAR;
	public static Typeface ROBOTO_LIGHT;
	
	public static void init(Context context) {
		ROBOTO_BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
		ROBOTO_REGULAR = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		ROBOTO_LIGHT = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
	}
	
}