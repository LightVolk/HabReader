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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PostsFullData;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostShowLoader;
import net.meiolania.apps.habrahabr.utils.*;

public class PostShowFragment extends SherlockFragment implements LoaderCallbacks<PostsFullData> {
    public final static String URL_ARGUMENT = "url";
    private final static int LOADER_POST = 0;
    private String url;
    private String title;
    private ProgressDialog progressDialog;
    private Preferences prefs;
    private WebView content;
    private FrameLayout webviewContainer;
    private static final String STYLESHEET = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
    private PostsFullData currentData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	setHasOptionsMenu(true);
	setRetainInstance(true);

	url = getArguments().getString(URL_ARGUMENT);

	content = (WebView) getSherlockActivity().findViewById(R.id.post_content);
	webviewContainer = (FrameLayout) getSherlockActivity().findViewById(R.id.webview_container);

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
		IntentUtils.createShareIntent(getSherlockActivity(), title, url);
		break;
        case R.id.save:
            if(currentData != null && getSherlockActivity() != null && ConnectionUtils.isConnected(getSherlockActivity()))
                savePost();
            break;
	}
	return super.onOptionsItemSelected(item);
    }

    private void savePost()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog(R.string.saving_post);
            }

            @Override
            protected Void doInBackground(Void... params) {
                PostsSaverUtils.savePost(currentData,getSherlockActivity());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();
            }
        }.execute();
    }

    @Override
    public Loader<PostsFullData> onCreateLoader(int id, Bundle args) {
        showProgressDialog(R.string.loading_post);

        PostShowLoader loader = new PostShowLoader(getSherlockActivity(), url);
        loader.forceLoad();

	return loader;
    }

    @SuppressLint("NewApi")
    @Override
    public void onLoadFinished(Loader<PostsFullData> loader, PostsFullData data) {
	if (getSherlockActivity() != null && data != null) {
	    ActionBar actionBar = getSherlockActivity().getSupportActionBar();
	    actionBar.setTitle(data.getTitle());

	    prefs = Preferences.getInstance(getSherlockActivity());

	    content.setWebViewClient(new HabrWebClient(getSherlockActivity()));
	    content.getSettings().setSupportZoom(true);

	    content.getSettings().setJavaScriptEnabled(true);
	    content.getSettings().setDefaultZoom(ZoomDensity.FAR);
	    content.setInitialScale(prefs.getViewScale(getSherlockActivity()));

	    content.loadDataWithBaseURL("", STYLESHEET + data.getContent(), "text/html", "UTF-8", null);

        currentData = data;
	}

	title = data.getTitle();

	hideProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<PostsFullData> loader) {
    }

    private void showProgressDialog(int resourceMessage) {
	progressDialog = new ProgressDialog(getSherlockActivity());
	progressDialog.setMessage(getString(resourceMessage));
	progressDialog.setCancelable(true);
	progressDialog.show();
    }

    private void hideProgressDialog() {
	if (progressDialog != null)
	    progressDialog.dismiss();
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
	super.onResume();

	content = (WebView) getSherlockActivity().findViewById(R.id.post_content);
	prefs = Preferences.getInstance(getSherlockActivity());

	if (UIUtils.isHoneycombOrHigher()) {
	    content.getSettings().setBuiltInZoomControls(true);
	    content.getSettings().setDisplayZoomControls(prefs.getPostsZoom());
	} else
	    content.getSettings().setBuiltInZoomControls(prefs.getPostsZoom());
    }

    @Override
    public void onDestroy() {
	super.onDestroy();

	// http://stackoverflow.com/a/8011027/921834
	webviewContainer.removeAllViews();
	content.destroy();
    }

}