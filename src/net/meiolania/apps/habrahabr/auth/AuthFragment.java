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

import java.io.IOException;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.HabrAuthApi;
import net.meiolania.apps.habrahabr.utils.ToastUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;

public class AuthFragment extends SherlockFragment {
	public static final String MAIN_URL = "http://habrahabr.ru/";
	public static final String LOGIN_URL = "http://habrahabr.ru/login/";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		showActionBar();
		showAuthPage();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.auth_fragment, container, false);
	}

	private void showActionBar() {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setTitle(R.string.auth);
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
	}

	private void showAuthPage() {
		WebView content = (WebView) getSherlockActivity().findViewById(R.id.auth_content);

		content.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				// We handle redirect to main page after auth
				if (url.equals(MAIN_URL)) {
					String cookies[] = CookieManager.getInstance().getCookie(MAIN_URL).split(";");

					Preferences preferences = Preferences.getInstance(getSherlockActivity());

					for (String cookie : cookies) {
						String cookieNameAndValue[] = cookie.split("=");
						String cookieName = cookieNameAndValue[0].trim();
						String cookieValue = cookieNameAndValue[1].trim();

						if (cookieName.equals(AuthApi.SESSION_ID))
							preferences.setPHPSessionId(cookieValue);
						if (cookieName.equals(AuthApi.AUTH_ID))
							preferences.setHSecId(cookieValue);
					}

					new GetUserName().execute();
				}
			}

		});
		content.getSettings().setSupportZoom(true);
		content.getSettings().setBuiltInZoomControls(true);
		content.getSettings().setJavaScriptEnabled(true);

		content.loadUrl(LOGIN_URL);
	}

	private class GetUserName extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO: handle html from webview?
			try {
				Preferences preferences = Preferences.getInstance(getSherlockActivity());
				
				Connection connection = Jsoup.connect(MAIN_URL);
				connection.cookie(AuthApi.AUTH_ID, HabrAuthApi.getInstance().getAuthId());
				connection.cookie(AuthApi.SESSION_ID, HabrAuthApi.getInstance().getSessionId());
				Document document = connection.get();

				Element usernameElement = document.select("a.username").first();
				preferences.setLogin(usernameElement.text());
			} catch (IOException e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(getSherlockActivity());
			progress.setMessage(getString(R.string.loading));
			progress.setCancelable(true);
			progress.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			progress.dismiss();

			ToastUtils.show(getSherlockActivity(), R.string.auth_success);

			Intent intent = new Intent(getSherlockActivity(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}

	}

}