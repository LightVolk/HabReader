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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.HubsSearchActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class HubsMainFragment extends SherlockFragment implements OnNavigationListener {
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setHasOptionsMenu(true);
		
		showActionBar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_hubs, container, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

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
	}

	private void showActionBar() {
		SherlockFragmentActivity activity = getSherlockActivity();

		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.removeAllTabs();
		actionBar.setTitle("");
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

		ft.replace(R.id.hubs_fragment, fragment);
		ft.commit();

		return false;
	}
}