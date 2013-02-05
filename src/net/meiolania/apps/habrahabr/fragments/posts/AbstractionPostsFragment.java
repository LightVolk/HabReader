/*
Copyright 2012 Andrey Zaytsev

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

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PostsSearchActivity;
import net.meiolania.apps.habrahabr.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostsLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class AbstractionPostsFragment extends SherlockListFragment implements OnScrollListener,
	LoaderCallbacks<ArrayList<PostsData>> {
    protected boolean isLoadData;
    protected int page;
    protected ArrayList<PostsData> posts;
    protected PostsAdapter adapter;
    protected boolean noMoreData;

    protected abstract String getUrl();

    protected abstract int getLoaderId();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	setRetainInstance(true);
	setHasOptionsMenu(true);

	if (adapter == null) {
	    posts = new ArrayList<PostsData>();
	    adapter = new PostsAdapter(getActivity(), posts);
	}

	setListAdapter(adapter);
	setListShown(true);

	getListView().setDivider(null);
	getListView().setDividerHeight(0);

	getListView().setOnScrollListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.posts_fragment, menu);

	final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
	searchQuery.setOnEditorActionListener(new OnEditorActionListener() {
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		    Intent intent = new Intent(getSherlockActivity(), PostsSearchActivity.class);
		    intent.putExtra(PostsSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
		    startActivity(intent);
		    return true;
		}
		return false;
	    }
	});

	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
	showPost(position);
    }

    protected void showPost(int position) {
	PostsData data = posts.get(position);

	Intent intent = new Intent(getSherlockActivity(), PostsShowActivity.class);
	intent.putExtra(PostsShowActivity.EXTRA_URL, data.getUrl());
	intent.putExtra(PostsShowActivity.EXTRA_TITLE, data.getTitle());

	startActivity(intent);
    }

    protected void restartLoading() {
	if (ConnectionUtils.isConnected(getSherlockActivity())) {
	    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

	    PostsLoader.setPage(++page);

	    getSherlockActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, this);

	    isLoadData = true;
	}
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	if ((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
	    restartLoading();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public Loader<ArrayList<PostsData>> onCreateLoader(int id, Bundle args) {
	PostsLoader loader = new PostsLoader(getSherlockActivity(), getUrl());
	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PostsData>> loader, ArrayList<PostsData> data) {
	if (data.isEmpty()) {
	    noMoreData = true;

	    if (getSherlockActivity() != null)
		Toast.makeText(getSherlockActivity(), R.string.no_more_pages, Toast.LENGTH_SHORT).show();
	}

	posts.addAll(data);
	adapter.notifyDataSetChanged();

	if (getSherlockActivity() != null)
	    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

	isLoadData = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PostsData>> loader) {

    }

}