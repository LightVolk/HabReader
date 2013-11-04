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

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class IntentUtils {

    public static void createShareIntent(Context context, String title, String url) {
	Preferences preferences = Preferences.getInstance(context);
	String shareText = preferences.getShareText();
	
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("text/plain");
	intent.putExtra(Intent.EXTRA_TEXT, shareText.replace("$link$", url).replace("$title$", title));

	context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }
    public static boolean isIntentAvailable(Context context, Intent intent)
    {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}