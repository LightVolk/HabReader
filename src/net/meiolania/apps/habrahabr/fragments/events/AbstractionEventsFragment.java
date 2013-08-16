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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.EventsShowActivity;
import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.data.EventsData;
import net.meiolania.apps.habrahabr.fragments.events.loader.EventLoader;
import net.meiolania.apps.habrahabr.ui.PageActionProvider;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class AbstractionEventsFragment extends SherlockListFragment
		implements OnScrollListener, LoaderCallbacks<ArrayList<EventsData>> {
	protected int page;
	protected boolean isLoadData;
	protected ArrayList<EventsData> events;
	protected EventsAdapter adapter;
	protected boolean noMoreData;
	protected boolean firstLoading = true;

	protected abstract String getUrl();

	protected abstract int getLoaderId();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		setRetainInstance(true);

		if (adapter == null) {
			events = new ArrayList<EventsData>();
			adapter = new EventsAdapter(getSherlockActivity(), events);
		}

		setListAdapter(adapter);

		if (firstLoading)
			setListShown(false);

		if (Preferences.getInstance(getSherlockActivity())
				.getAdditionalEvents()) {
			getListView().setDivider(null);
			getListView().setDividerHeight(0);
		}

		getListView().setOnScrollListener(this);

		setEmptyText(getString(R.string.no_items_events));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.events_fragment, menu);

		PageActionProvider pageActionProvider = (PageActionProvider) menu
				.findItem(R.id.page).getActionProvider();
		pageActionProvider.setPage(page);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		showEvent(position);
	}

	protected void showEvent(int position) {
		EventsData data = events.get(position);

		Intent intent = new Intent(getSherlockActivity(),
				EventsShowActivity.class);
		intent.putExtra(EventsShowActivity.EXTRA_TITLE, data.getTitle());
		intent.putExtra(EventsShowActivity.EXTRA_URL, data.getUrl());

		startActivity(intent);
	}

	protected void restartLoading() {
		if (ConnectionUtils.isConnected(getSherlockActivity())) {
			if (!firstLoading)
				getSherlockActivity()
						.setSupportProgressBarIndeterminateVisibility(true);

			EventLoader.setPage(++page);

			getSherlockActivity().getSupportLoaderManager().restartLoader(
					getLoaderId(), null, this);

			isLoadData = true;
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount
				&& !isLoadData && !noMoreData)
			restartLoading();
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public Loader<ArrayList<EventsData>> onCreateLoader(int id, Bundle args) {
		EventLoader loader = new EventLoader(getSherlockActivity(), getUrl());
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<EventsData>> loader,
			ArrayList<EventsData> data) {
		if (data.isEmpty())
			noMoreData = true;

		events.addAll(data);
		adapter.notifyDataSetChanged();

		firstLoading = false;

		if (getSherlockActivity() != null)
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					false);

		isLoadData = false;

		if (getSherlockActivity() != null) {
			setListShown(true);
			getSherlockActivity().invalidateOptionsMenu();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<EventsData>> loader) {

	}

}