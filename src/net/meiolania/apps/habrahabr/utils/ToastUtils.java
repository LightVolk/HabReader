/*
Copyright 2012-2013 Andrey Zaytsev, Sergey Ivanov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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