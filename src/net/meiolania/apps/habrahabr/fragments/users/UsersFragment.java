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

package net.meiolania.apps.habrahabr.fragments.users;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.UsersSearchActivity;
import net.meiolania.apps.habrahabr.activities.UsersShowActivity;
import net.meiolania.apps.habrahabr.adapters.UserAdapter;
import net.meiolania.apps.habrahabr.data.UsersData;
import net.meiolania.apps.habrahabr.fragments.users.loader.UsersLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class UsersFragment extends SherlockListFragment implements LoaderCallbacks<ArrayList<UsersData>> {
    public final static String URL_ARGUMENT = "url";
    public final static int LOADER_USER = 25;
    private ArrayList<UsersData> users;
    private UserAdapter adapter;
    private String url;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	
	showActionBar();

	if (getArguments() != null)
	    url = getArguments().getString(URL_ARGUMENT);

	setRetainInstance(true);
	setHasOptionsMenu(true);

	if (adapter == null) {
	    users = new ArrayList<UsersData>();
	    adapter = new UserAdapter(getSherlockActivity(), users);
	}

	setListAdapter(adapter);
	setListShown(true);

	if (ConnectionUtils.isConnected(getSherlockActivity()))
	    getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_USER, null, this);
    }

    private void showActionBar() {
	ActionBar actionBar = getSherlockActivity().getSupportActionBar();
	actionBar.removeAllTabs();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(R.string.people);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.users_fragment, menu);

	final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
	searchQuery.setOnEditorActionListener(new OnEditorActionListener() {
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		    Intent intent = new Intent(getSherlockActivity(), UsersSearchActivity.class);
		    intent.putExtra(UsersSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
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
	showUser(position);
    }

    protected void showUser(int position) {
	UsersData data = users.get(position);

	Intent intent = new Intent(getSherlockActivity(), UsersShowActivity.class);
	intent.putExtra(UsersShowActivity.EXTRA_NAME, data.getName());
	intent.putExtra(UsersShowActivity.EXTRA_URL, data.getUrl());

	startActivity(intent);
    }

    @Override
    public Loader<ArrayList<UsersData>> onCreateLoader(int id, Bundle args) {
	getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

	UsersLoader loader = null;

	if (url == null)
	    loader = new UsersLoader(getSherlockActivity());
	else
	    loader = new UsersLoader(getSherlockActivity(), url);

	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<UsersData>> loader, ArrayList<UsersData> data) {
	users.addAll(data);
	adapter.notifyDataSetChanged();

	if (getSherlockActivity() != null)
	    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<UsersData>> loader) {

    }

}