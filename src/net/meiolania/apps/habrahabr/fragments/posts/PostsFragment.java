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

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PostsSearchActivity;
import net.meiolania.apps.habrahabr.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.api.HabrAuthApi;
import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostsLoader;
import net.meiolania.apps.habrahabr.ui.PageActionProvider;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class PostsFragment extends SherlockFragment implements OnScrollListener, OnItemClickListener,
		LoaderCallbacks<List<PostEntry>> {
	public final static int LOADER_ID = 0;
	private boolean isLoadData;
	private boolean noMoreData;
	private List<PostEntry> posts;
	private PostsAdapter adapter;
	private ListView listView;
	private int page = 1;
	
	public abstract List<PostEntry> getPosts(int page);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fr_posts, container, false);

		listView = (ListView) view.findViewById(R.id.postsList);

		return view;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setRetainInstance(true);
		setHasOptionsMenu(true);

		if (adapter == null) {
			posts = new ArrayList<PostEntry>();
			adapter = new PostsAdapter(getActivity(), HabrAuthApi.getInstance(), posts);
		}

		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.posts_fragment, menu);

		final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
		searchQuery.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					Intent intent = new Intent(getSherlockActivity(), PostsSearchActivity.class);
					intent.putExtra(PostsSearchFragment.EXTRA_QUERY, searchQuery.getText().toString());
					startActivity(intent);
					return true;
				}
				return false;
			}
		});

		PageActionProvider pageActionProvider = (PageActionProvider) menu.findItem(R.id.page).getActionProvider();
		pageActionProvider.setPage(page);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		showPost(position);
	}

	protected void showPost(int position) {
		PostEntry data = posts.get(position);

		Intent intent = new Intent(getSherlockActivity(), PostsShowActivity.class);
		intent.putExtra(PostsShowActivity.EXTRA_URL, data.getUrl());
		intent.putExtra(PostsShowActivity.EXTRA_TITLE, data.getTitle());

		startActivity(intent);
	}

	protected void restartLoading() {
		if (ConnectionUtils.isConnected(getSherlockActivity())) {
			LoaderManager loaderManager = getSherlockActivity().getSupportLoaderManager();
			loaderManager.restartLoader(LOADER_ID, null, this);

			isLoadData = true;
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData && !noMoreData)
			restartLoading();
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public Loader<List<PostEntry>> onCreateLoader(int id, Bundle args) {
		PostsLoader loader = new PostsLoader(getSherlockActivity(), this, page);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<PostEntry>> loader, List<PostEntry> data) {
		// TODO: переделать. Возможно просто произошёл обрыв соединения
		if (data.isEmpty())
			noMoreData = true;

		posts.addAll(data);

		adapter.notifyDataSetChanged();

		isLoadData = false;

		if (getSherlockActivity() != null)
			getSherlockActivity().supportInvalidateOptionsMenu();
	}

	@Override
	public void onLoaderReset(Loader<List<PostEntry>> loader) {

	}

}