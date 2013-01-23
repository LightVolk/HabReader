package net.meiolania.apps.habrahabr.utils;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtils {
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    public static void show(Activity activity, String message, int duration) {
	Toast toast = Toast.makeText(activity, message, duration);
	toast.show();
    }

    public static void show(Activity activity, String message) {
	show(activity, message, LENGTH_LONG);
    }

    public static void show(Activity activity, int resId) {
	show(activity, activity.getString(resId), LENGTH_LONG);
    }

}