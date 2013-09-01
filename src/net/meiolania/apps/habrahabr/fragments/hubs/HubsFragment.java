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

package net.meiolania.apps.habrahabr.fragments.hubs;

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.HubsShowActivity;
import net.meiolania.apps.habrahabr.adapters.HubsAdapter;
import net.meiolania.apps.habrahabr.api.hubs.HubEntry;
import net.meiolania.apps.habrahabr.fragments.hubs.loader.HubsLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class HubsFragment extends SherlockListFragment implements OnScrollListener, LoaderCallbacks<List<HubEntry>> {
	public final static int LOADER_HUBS = 0;
	public final static String URL_ARGUMENT = "url";
	private List<HubEntry> hubs;
	private HubsAdapter adapter;
	private int page = 1;
	private boolean isLoadData;
	private String url;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getArguments() != null)
			url = getArguments().getString(URL_ARGUMENT);
		else
			url = "http://habrahabr.ru/hubs/page%page%/";

		if (adapter == null) {
			hubs = new ArrayList<HubEntry>();
			adapter = new HubsAdapter(getSherlockActivity(), hubs);
		}

		setListAdapter(adapter);
		setListShown(false);

		getListView().setOnScrollListener(this);

		setEmptyText(getString(R.string.no_items_hubs));
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		showHub(position);
	}

	protected void showHub(int position) {
		HubEntry data = hubs.get(position);

		Intent intent = new Intent(getSherlockActivity(), HubsShowActivity.class);
		intent.putExtra(HubsShowActivity.EXTRA_URL, data.getUrl());
		intent.putExtra(HubsShowActivity.EXTRA_TITLE, data.getTitle());

		startActivity(intent);
	}

	protected void restartLoading() {
		LoaderManager loaderManager = getSherlockActivity().getSupportLoaderManager();
		loaderManager.restartLoader(LOADER_HUBS, null, this);
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
			restartLoading();
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public Loader<List<HubEntry>> onCreateLoader(int id, Bundle args) {
		HubsLoader loader = new HubsLoader(getSherlockActivity(), url, page);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<HubEntry>> loader, List<HubEntry> data) {
		if (data != null) {
			hubs.addAll(data);
			adapter.notifyDataSetChanged();
		}

		isLoadData = false;
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<List<HubEntry>> loader) {
	}

}