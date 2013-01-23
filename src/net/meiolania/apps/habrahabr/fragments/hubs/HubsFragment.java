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

package net.meiolania.apps.habrahabr.fragments.hubs;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.HubsSearchActivity;
import net.meiolania.apps.habrahabr.activities.HubsShowActivity;
import net.meiolania.apps.habrahabr.adapters.HubsAdapter;
import net.meiolania.apps.habrahabr.data.HubsData;
import net.meiolania.apps.habrahabr.fragments.hubs.loader.HubsLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class HubsFragment extends SherlockListFragment implements OnNavigationListener, OnScrollListener,
	LoaderCallbacks<ArrayList<HubsData>> {
    public final static int LIST_ALL_HUBS = 0;
    public final static int LIST_API = 1;
    public final static int LIST_ADMIN = 2;
    public final static int LIST_DB = 3;
    public final static int LIST_SECURITY = 4;
    public final static int LIST_DESIGN = 5;
    public final static int LIST_GADGETS = 6;
    public final static int LIST_COMPANIES = 7;
    public final static int LIST_MANAGEMENT = 8;
    public final static int LIST_PROGRAMMING = 9;
    public final static int LIST_SOFTWARE = 10;
    public final static int LIST_TELECOMMUNICATIONS = 11;
    public final static int LIST_FRAMEWORKS = 12;
    public final static int LIST_FRONTEND = 13;
    public final static int LIST_OTHERS = 14;

    public final static int LOADER_HUBS = 0;
    public final static String URL_ARGUMENT = "url";
    private ArrayList<HubsData> hubs;
    private HubsAdapter adapter;
    private int page;
    private boolean isLoadData;
    private String url;
    private boolean noMoreData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	//showActionBar();

	setHasOptionsMenu(true);

	if (getArguments() != null)
	    url = getArguments().getString(URL_ARGUMENT);
	else
	    url = "http://habrahabr.ru/hubs/page%page%/";

	if (adapter == null) {
	    hubs = new ArrayList<HubsData>();
	    adapter = new HubsAdapter(getSherlockActivity(), hubs);
	}

	setListAdapter(adapter);
	setListShown(true);

	getListView().setOnScrollListener(this);
    }

    private void showActionBar() {
	SherlockFragmentActivity activity = getSherlockActivity();

	ActionBar actionBar = activity.getSupportActionBar();
	actionBar.removeAllTabs();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

	ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(activity.getSupportActionBar().getThemedContext(),
		R.array.hubs_list, R.layout.sherlock_spinner_item);
	list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
	actionBar.setListNavigationCallbacks(list, this);
    }

    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	HubsFragment fragment = new HubsFragment();
	FragmentTransaction ft = getFragmentManager().beginTransaction();

	Bundle arguments = new Bundle();
	switch ((int) itemId) {
	default:
	case LIST_ALL_HUBS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/page%page%/");
	    break;
	case LIST_API:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/api/page%page%/");
	    break;
	case LIST_ADMIN:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/administration/page%page%/");
	    break;
	case LIST_DB:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/databases/page%page%/");
	    break;
	case LIST_SECURITY:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/security/page%page%/");
	    break;
	case LIST_DESIGN:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/design-and-media/page%page%/");
	    break;
	case LIST_GADGETS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/hardware/page%page%/");
	    break;
	case LIST_COMPANIES:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/companies-and-services/page%page%/");
	    break;
	case LIST_MANAGEMENT:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/management/page%page%/");
	    break;
	case LIST_PROGRAMMING:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/programming/page%page%/");
	    break;
	case LIST_SOFTWARE:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/software/page%page%/");
	    break;
	case LIST_TELECOMMUNICATIONS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/telecommunications/page%page%/");
	    break;
	case LIST_FRAMEWORKS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/fw-and-cms/page%page%/");
	    break;
	case LIST_FRONTEND:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/frontend/page%page%/");
	    break;
	case LIST_OTHERS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/others/page%page%/");
	    break;
	}

	fragment.setArguments(arguments);
	ft.replace(R.id.content_frame, fragment);
	ft.commit();
	return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.hubs_fragment, menu);

	final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
	searchQuery.setOnEditorActionListener(new OnEditorActionListener() {
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		    Intent intent = new Intent(getSherlockActivity(), HubsSearchActivity.class);
		    intent.putExtra(HubsSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
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
	showHub(position);
    }

    protected void showHub(int position) {
	HubsData data = hubs.get(position);

	Intent intent = new Intent(getSherlockActivity(), HubsShowActivity.class);
	intent.putExtra(HubsShowActivity.EXTRA_URL, data.getUrl());
	intent.putExtra(HubsShowActivity.EXTRA_TITLE, data.getTitle());

	startActivity(intent);
    }

    protected void restartLoading() {
	if (ConnectionUtils.isConnected(getSherlockActivity())) {
	    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

	    HubsLoader.setPage(++page);

	    getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_HUBS, null, this);

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
    public Loader<ArrayList<HubsData>> onCreateLoader(int id, Bundle args) {
	HubsLoader loader = new HubsLoader(getSherlockActivity(), url);
	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<HubsData>> loader, ArrayList<HubsData> data) {
	if (data.isEmpty()) {
	    noMoreData = true;

	    Toast.makeText(getSherlockActivity(), R.string.no_more_pages, Toast.LENGTH_SHORT).show();
	}

	hubs.addAll(data);
	adapter.notifyDataSetChanged();

	if (getSherlockActivity() != null)
	    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

	isLoadData = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<HubsData>> loader) {
    }

}