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

package net.meiolania.apps.habrahabr.fragments;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.HabrAuthApi;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import net.meiolania.apps.habrahabr.adapters.SideMenuAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class SideMenuFragment extends SherlockListFragment {
	private ArrayList<SideMenuData> menu;
	private BaseAdapter menuAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		addMenu();

		setListAdapter(menuAdapter);

		getListView().setBackgroundColor(Color.WHITE);
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
	}

	private void addMenu() {
		menu = new ArrayList<SideMenuData>();

		if (!HabrAuthApi.getInstance().isAuth())
			menu.add(new SideMenuData(R.string.auth, R.drawable.ic_users, ItemType.AUTH, false));
		else {
			menu.add(new SideMenuData(R.string.account, 0, null, true));
			menu.add(new SideMenuData(HabrAuthApi.getInstance().getLogin(), R.drawable.ic_users, ItemType.PROFILE, false));
			menu.add(new SideMenuData(R.string.feed, R.drawable.ic_feed, ItemType.FEED, false));
			menu.add(new SideMenuData(R.string.favorites, R.drawable.ic_favorites, ItemType.FAVORITES, false));
			menu.add(new SideMenuData(R.string.conversations, R.drawable.ic_conversations, ItemType.CONVERSATIONS, false));
		}

		menu.add(new SideMenuData(R.string.sections, 0, null, true));
		menu.add(new SideMenuData(R.string.posts, R.drawable.ic_posts, ItemType.POSTS, false));
		menu.add(new SideMenuData(R.string.hubs, R.drawable.ic_hubs, ItemType.HUBS, false));
		menu.add(new SideMenuData(R.string.qa, R.drawable.ic_qa, ItemType.QA, false));
		menu.add(new SideMenuData(R.string.events, R.drawable.ic_events, ItemType.EVENTS, false));
		menu.add(new SideMenuData(R.string.companies, R.drawable.ic_companies, ItemType.COMPANIES, false));
		menu.add(new SideMenuData(R.string.people, R.drawable.ic_users, ItemType.USERS, false));

		menuAdapter = new SideMenuAdapter(getSherlockActivity(), menu);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		SideMenuData data = menu.get(position);

		if (data.isSection && data.itemType == null)
			return;

		switchFragment(data.itemType);
	}

	private void switchFragment(ItemType contentType) {
		if (getSherlockActivity() instanceof MainActivity) {
			MainActivity activity = (MainActivity) getSherlockActivity();
			activity.switchContent(contentType);
		}
	}

	public enum ItemType {
		AUTH, PROFILE, FEED, FAVORITES, POSTS, HUBS, QA, EVENTS, COMPANIES, USERS, CONVERSATIONS
	};

	public class SideMenuData {
		public String title;
		public int icon;
		public ItemType itemType;
		public boolean isSection;

		public SideMenuData(int title, int icon, ItemType itemType, boolean isSection) {
			this(getString(title), icon, itemType, isSection);
		}

		public SideMenuData(String title, int icon, ItemType itemType, boolean isSection) {
			this.title = title;
			this.icon = icon;
			this.itemType = itemType;
			this.isSection = isSection;
		}
	}
}