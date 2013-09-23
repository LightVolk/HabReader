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

package net.meiolania.apps.habrahabr.fragments.events;

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.EventsShowActivity;
import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.api.events.EventEntry;
import net.meiolania.apps.habrahabr.fragments.events.loader.EventLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class EventsFragment extends SherlockFragment implements OnScrollListener, OnItemClickListener,
		LoaderCallbacks<List<EventEntry>> {
	public final static int LOADER_ID = 0;
	private boolean isLoadData;
	private ArrayList<EventEntry> events;
	private EventsAdapter adapter;
	private boolean noMoreData;
	private ListView listView;
	private int page = 1;

	public abstract List<EventEntry> getEvents(int page);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (adapter == null) {
			events = new ArrayList<EventEntry>();
			adapter = new EventsAdapter(getSherlockActivity(), events);
		}

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fr_events, container, false);

		listView = (ListView) view.findViewById(R.id.eventsList);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		showEvent(position);
	}

	protected void showEvent(int position) {
		EventEntry data = events.get(position);

		Intent intent = new Intent(getSherlockActivity(), EventsShowActivity.class);
		intent.putExtra(EventsShowActivity.EXTRA_TITLE, data.getTitle());
		intent.putExtra(EventsShowActivity.EXTRA_URL, data.getUrl());

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
	public Loader<List<EventEntry>> onCreateLoader(int id, Bundle args) {
		EventLoader loader = new EventLoader(getSherlockActivity(), this, page);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<EventEntry>> loader, List<EventEntry> data) {
		if (data.isEmpty())
			noMoreData = true;

		events.addAll(data);
		adapter.notifyDataSetChanged();

		isLoadData = false;
	}

	@Override
	public void onLoaderReset(Loader<List<EventEntry>> loader) {

	}

}