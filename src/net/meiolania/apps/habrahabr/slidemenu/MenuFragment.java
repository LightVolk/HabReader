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

package net.meiolania.apps.habrahabr.slidemenu;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import net.meiolania.apps.habrahabr.activities.UsersShowActivity;
import net.meiolania.apps.habrahabr.auth.AuthFragment;
import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.fragments.companies.CompaniesFragment;
import net.meiolania.apps.habrahabr.fragments.events.EventsMainFragment;
import net.meiolania.apps.habrahabr.fragments.favorites.FavoritesMainFragment;
import net.meiolania.apps.habrahabr.fragments.feed.FeedMainFragment;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsMainFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaMainFragment;
import net.meiolania.apps.habrahabr.fragments.users.UsersFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MenuFragment extends SherlockListFragment {
	private ArrayList<MenuData> menu;
	private MenuAdapter menuAdapter;

	public enum ItemType {
		AUTH, PROFILE, FEED, FAVORITES, POSTS, HUBS, QA, EVENTS, COMPANIES, USERS
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		menu = new ArrayList<MenuData>();

		if (!User.getInstance().isLogged())
			menu.add(new MenuData(R.string.auth, R.drawable.ic_users, ItemType.AUTH, false));
		else {
			menu.add(new MenuData(R.string.account, 0, null, true));
			// TODO: set user avatar
			menu.add(new MenuData(User.getInstance().getLogin(), R.drawable.ic_users, ItemType.PROFILE, false));
			menu.add(new MenuData(R.string.feed, R.drawable.ic_feed, ItemType.FEED, false));
			menu.add(new MenuData(R.string.favorites, R.drawable.ic_favorites, ItemType.FAVORITES, false));
		}

		menu.add(new MenuData(R.string.sections, 0, null, true));
		menu.add(new MenuData(R.string.posts, R.drawable.ic_posts, ItemType.POSTS, false));
		menu.add(new MenuData(R.string.hubs, R.drawable.ic_hubs, ItemType.HUBS, false));
		menu.add(new MenuData(R.string.qa, R.drawable.ic_qa, ItemType.QA, false));
		menu.add(new MenuData(R.string.events, R.drawable.ic_events, ItemType.EVENTS, false));
		menu.add(new MenuData(R.string.companies, R.drawable.ic_companies, ItemType.COMPANIES, false));
		menu.add(new MenuData(R.string.people, R.drawable.ic_users, ItemType.USERS, false));

		menuAdapter = new MenuAdapter(getSherlockActivity(), menu);
		setListAdapter(menuAdapter);

		getListView().setBackgroundColor(Color.WHITE);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		MenuData data = menu.get(position);

		if (data.isSection && data.itemType == null)
			return;

		Fragment newContent = null;
		switch (data.itemType) {
			case AUTH:
				newContent = new AuthFragment();
				break;
			case PROFILE:
				Intent intent = new Intent(getSherlockActivity(), UsersShowActivity.class);
				intent.putExtra(UsersShowActivity.EXTRA_URL, "http://habrahabr.ru/users/" + User.getInstance().getLogin());
				getSherlockActivity().startActivity(intent);
				break;
			case FEED:
				newContent = new FeedMainFragment();
				break;
			case FAVORITES:
				newContent = new FavoritesMainFragment();
				break;
			case POSTS:
				newContent = new PostsMainFragment();
				break;
			case HUBS:
				newContent = new HubsMainFragment();
				break;
			case QA:
				newContent = new QaMainFragment();
				break;
			case EVENTS:
				newContent = new EventsMainFragment();
				break;
			case COMPANIES:
				newContent = new CompaniesFragment();
				break;
			case USERS:
				newContent = new UsersFragment();
				break;
		}
		if (newContent != null)
			switchFragment(newContent, data.itemType);
	}

	private void switchFragment(Fragment fragment, ItemType contentType) {
		if (getSherlockActivity() == null)
			return;

		if (getSherlockActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getSherlockActivity();
			fca.switchContent(fragment, contentType);
		}
	}

	private class MenuData {
		public String title;
		public int icon;
		public ItemType itemType;
		public boolean isSection;

		public MenuData(int title, int icon, ItemType itemType, boolean isSection) {
			this(getString(title), icon, itemType, isSection);
		}

		public MenuData(String title, int icon, ItemType itemType, boolean isSection) {
			this.title = title;
			this.icon = icon;
			this.itemType = itemType;
			this.isSection = isSection;
		}
	}

	private class MenuAdapter extends BaseAdapter {
		private ArrayList<MenuData> data;
		private Context context;
		private LayoutInflater layoutInflater;

		public MenuAdapter(Context context, ArrayList<MenuData> data) {
			this.data = data;
			this.context = context;
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public MenuData getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuData data = getItem(position);

			if (!data.isSection) {

				View view = layoutInflater.inflate(R.layout.slide_menu_row, null);

				TextView title = (TextView) view.findViewById(R.id.slide_menu_title);

				Drawable img = context.getResources().getDrawable(data.icon);
				title.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

				title.setText(data.title);

				return view;
			} else {
				View view = layoutInflater.inflate(R.layout.slide_menu_section_row, null);

				TextView title = (TextView) view.findViewById(R.id.slide_menu_title);

				title.setText(data.title);

				return view;
			}
		}

	}
}