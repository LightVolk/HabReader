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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class EventsMainFragment extends SherlockFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		showActionBar();
	}

	private void showActionBar() {
		SherlockFragmentActivity activity = getSherlockActivity();

		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.removeAllTabs();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.events);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Preferences preferences = Preferences.getInstance(activity);
		int selectedTab = preferences.getEventsDefaultTab();

		/* Coming tab */
		Tab tab = actionBar.newTab();
		tab.setText(R.string.coming);
		tab.setTag("coming");
		tab.setTabListener(new TabListener<EventComingFragment>(activity,
				"coming", EventComingFragment.class));
		actionBar.addTab(tab, selectedTab == 0 ? true : false);

		/* Current tab */
		tab = actionBar.newTab();
		tab.setText(R.string.current);
		tab.setTag("current");
		tab.setTabListener(new TabListener<EventCurrentFragment>(activity,
				"current", EventCurrentFragment.class));
		actionBar.addTab(tab, selectedTab == 1 ? true : false);

		/* Past tab */
		tab = actionBar.newTab();
		tab.setText(R.string.past);
		tab.setTag("past");
		tab.setTabListener(new TabListener<EventPastFragment>(activity, "past",
				EventPastFragment.class));
		actionBar.addTab(tab, selectedTab == 2 ? true : false);
	}
}