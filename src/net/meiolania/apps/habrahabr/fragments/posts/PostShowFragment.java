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

package net.meiolania.apps.habrahabr.fragments.posts;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostShowLoader;
import net.meiolania.apps.habrahabr.ui.HabrWebClient;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import net.meiolania.apps.habrahabr.utils.IntentUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PostShowFragment extends SherlockFragment implements LoaderCallbacks<PostEntry> {
	public final static String URL_ARGUMENT = "url";
	public final static int LOADER_POST = 0;
	private String url;
	private String title;
	private WebView webViewContent;
	private FrameLayout webViewContainer;
	private static final String STYLESHEET = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		setRetainInstance(true);

		url = getArguments().getString(URL_ARGUMENT);

		LoaderManager loaderManager = getSherlockActivity().getSupportLoaderManager();
		loaderManager.initLoader(LOADER_POST, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.posts_show_activity, container, false);

		webViewContent = (WebView) view.findViewById(R.id.post_content);
		webViewContainer = (FrameLayout) view.findViewById(R.id.webview_container);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.posts_show_activity, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.share:
				IntentUtils.createShareIntent(getSherlockActivity(), title, url);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<PostEntry> onCreateLoader(int id, Bundle args) {
		PostShowLoader loader = new PostShowLoader(getSherlockActivity(), url);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<PostEntry> loader, PostEntry data) {
		if (data != null) {
			ActionBar actionBar = getSherlockActivity().getSupportActionBar();
			actionBar.setTitle(data.getTitle());
			
			webViewContent.setWebViewClient(new HabrWebClient(getSherlockActivity()));
			webViewContent.getSettings().setSupportZoom(true);
			webViewContent.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			webViewContent.setInitialScale(Preferences.getInstance(getSherlockActivity()).getViewScale(getSherlockActivity()));

			webViewContent.loadDataWithBaseURL("", STYLESHEET + data.getText(), "text/html", "UTF-8", null);
		}
	}

	@Override
	public void onLoaderReset(Loader<PostEntry> loader) {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// http://stackoverflow.com/a/8011027/921834
		webViewContainer.removeAllViews();
		webViewContent.destroy();
	}

}