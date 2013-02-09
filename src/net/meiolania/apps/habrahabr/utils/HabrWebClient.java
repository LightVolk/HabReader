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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.activities.UsersShowActivity;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HabrWebClient extends WebViewClient {
    Context context;

    public HabrWebClient(Context context) {
	super();
	this.context = context;
    }

    public void onScaleChanged(WebView view, float oldScale, float newScale) {
	Preferences.getInstance(context).setViewScale(context, newScale);
    }

    public void onPageFinished(WebView view, String Url) {
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	// Allow the OS to handle links
	if (url.startsWith("mailto:")) {
	    MailTo mt = MailTo.parse(url);
	    Intent i = newEmailIntent(context, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
	    context.startActivity(i);
	} else if (url.startsWith("http://habrahabr.ru/post/")) {
	    Intent intent = new Intent(context, PostsShowActivity.class);
	    intent.putExtra(PostsShowActivity.EXTRA_URL, url);
	    context.startActivity(intent);
	} else if (url.startsWith("http://habrahabr.ru/users/")) {
	    Intent intent = new Intent(context, UsersShowActivity.class);
	    intent.putExtra(UsersShowActivity.EXTRA_URL, url);
	    context.startActivity(intent);
	} else {
	    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    context.startActivity(intent);
	}
	return true;
    }

    public static Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
	intent.putExtra(Intent.EXTRA_TEXT, body);
	intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	intent.putExtra(Intent.EXTRA_CC, cc);
	intent.setType("plain/text");
	return intent;
    }
}