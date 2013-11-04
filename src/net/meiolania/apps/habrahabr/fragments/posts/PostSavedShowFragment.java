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
import android.os.Bundle;
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
import net.meiolania.apps.habrahabr.utils.HabrWebClient;
import net.meiolania.apps.habrahabr.utils.IntentUtils;
import net.meiolania.apps.habrahabr.utils.UIUtils;

public class PostSavedShowFragment extends SherlockFragment  {
    public final static String DATA_ARGUMENT = "data";
    private Preferences prefs;
    private WebView content;
    private FrameLayout webviewContainer;
    private static final String STYLESHEET = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
    private PostsFullData data;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        data = getArguments().getParcelable(DATA_ARGUMENT);

        content = (WebView) getSherlockActivity().findViewById(R.id.post_content);
        webviewContainer = (FrameLayout) getSherlockActivity().findViewById(R.id.webview_container);

        if (getSherlockActivity() != null) {
            ActionBar actionBar = getSherlockActivity().getSupportActionBar();
            actionBar.setTitle(data.getTitle());

            prefs = Preferences.getInstance(getSherlockActivity());

            content.setWebViewClient(new HabrWebClient(getSherlockActivity()));
            content.getSettings().setSupportZoom(true);

            content.getSettings().setJavaScriptEnabled(true);
            content.getSettings().setDefaultZoom(ZoomDensity.FAR);
            content.setInitialScale(prefs.getViewScale(getSherlockActivity()));

            content.loadDataWithBaseURL("", STYLESHEET + data.getContent(), "text/html", "UTF-8", null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.posts_show_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.posts_saved, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                IntentUtils.createShareIntent(getSherlockActivity(), data.getTitle(), data.getUrl());
                break;
        }
        return super.onOptionsItemSelected(item);
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