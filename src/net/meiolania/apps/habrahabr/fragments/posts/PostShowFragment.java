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
import net.meiolania.apps.habrahabr.data.PostsFullData;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostShowLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import net.meiolania.apps.habrahabr.utils.HabrWebClient;
import net.meiolania.apps.habrahabr.utils.IntentUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PostShowFragment extends SherlockFragment implements LoaderCallbacks<PostsFullData> {
    public final static String URL_ARGUMENT = "url";
    private final static int LOADER_POST = 0;
    private String url;
    private ProgressDialog progressDialog;
    private PostsFullData data;
    private Preferences prefs;
    private WebView content;
    private static final String STYLESHEET = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	setHasOptionsMenu(true);
	setRetainInstance(true);

	url = getArguments().getString(URL_ARGUMENT);

	if (ConnectionUtils.isConnected(getSherlockActivity()))
	    getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_POST, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.posts_show_activity, container, false);
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
	    IntentUtils.createShareIntent(getSherlockActivity(), data.getTitle(), url);
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<PostsFullData> onCreateLoader(int id, Bundle args) {
	showProgressDialog();

	PostShowLoader loader = new PostShowLoader(getSherlockActivity(), url);
	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<PostsFullData> loader, PostsFullData data) {
	if (getSherlockActivity() != null) {
	    ActionBar actionBar = getSherlockActivity().getSupportActionBar();
	    actionBar.setTitle(data.getTitle());

	    content = (WebView) getSherlockActivity().findViewById(R.id.post_content);

	    prefs = Preferences.getInstance(getSherlockActivity());

	    content.setWebViewClient(new HabrWebClient(getSherlockActivity()));
	    content.getSettings().setSupportZoom(true);
	    content.getSettings().setBuiltInZoomControls(prefs.getPostsZoom());
	    content.getSettings().setJavaScriptEnabled(true);
	    content.setInitialScale(Preferences.getInstance(getSherlockActivity()).getViewScale(getSherlockActivity()));
	    content.getSettings().setDefaultZoom(ZoomDensity.FAR);

	    content.loadDataWithBaseURL("", STYLESHEET + data.getContent(), "text/html", "UTF-8", null);
	}

	this.data = data;

	hideProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<PostsFullData> loader) {
    }

    private void showProgressDialog() {
	progressDialog = new ProgressDialog(getSherlockActivity());
	progressDialog.setMessage(getString(R.string.loading_post));
	progressDialog.setCancelable(true);
	progressDialog.show();
    }

    private void hideProgressDialog() {
	if (progressDialog != null)
	    progressDialog.dismiss();
    }

    @Override
    public void onResume() {
	super.onResume();
	content = (WebView) getSherlockActivity().findViewById(R.id.post_content);
	prefs = Preferences.getInstance(getSherlockActivity());
	content.getSettings().setBuiltInZoomControls(prefs.getPostsZoom());
    }

}