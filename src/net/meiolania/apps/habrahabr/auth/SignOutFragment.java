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

package net.meiolania.apps.habrahabr.auth;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.actionbarsherlock.app.SherlockFragment;

public class SignOutFragment extends SherlockFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Preferences preferences = Preferences
				.getInstance(getSherlockActivity());
		preferences.setPHPSessionId(null);
		preferences.setLogin(null);
		preferences.setHSecId(null);

		CookieSyncManager.createInstance(getSherlockActivity());
		CookieManager.getInstance().removeAllCookie();

		User.getInstance().init(getSherlockActivity());

		Intent intent = new Intent(getSherlockActivity(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

}