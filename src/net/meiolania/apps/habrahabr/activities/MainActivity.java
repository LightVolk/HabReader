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

package net.meiolania.apps.habrahabr.activities;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.auth.AuthFragment;
import net.meiolania.apps.habrahabr.auth.SignOutFragment;
import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.fragments.companies.CompaniesFragment;
import net.meiolania.apps.habrahabr.fragments.events.EventsMainFragment;
import net.meiolania.apps.habrahabr.fragments.favorites.FavoritesMainFragment;
import net.meiolania.apps.habrahabr.fragments.feed.FeedMainFragment;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsMainFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaMainFragment;
import net.meiolania.apps.habrahabr.fragments.users.UsersFragment;
import net.meiolania.apps.habrahabr.slidemenu.MenuFragment;
import net.meiolania.apps.habrahabr.slidemenu.MenuFragment.ItemType;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends AbstractionFragmentActivity {
	private Fragment content;
	private MenuFragment.ItemType contentType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ac_main);
		
		initNavigationDrawer();
		
		int currentSection = -1;
		if (savedInstanceState != null)
			currentSection = savedInstanceState.getInt("currentSection");

		// @TODO: think of something new.
		switch (currentSection) {
			case 0:
				content = new AuthFragment();
				contentType = ItemType.AUTH;
				break;
			case 1:

				contentType = ItemType.PROFILE;
				break;
			case 2:
				content = new FeedMainFragment();
				contentType = ItemType.FEED;
				break;
			case 3:
				content = new FavoritesMainFragment();
				contentType = ItemType.FAVORITES;
				break;
			case 4:
				content = new PostsMainFragment();
				contentType = ItemType.POSTS;
				break;
			case 5:
				content = new HubsMainFragment();
				contentType = ItemType.HUBS;
				break;
			case 6:
				content = new QaMainFragment();
				contentType = ItemType.QA;
				break;
			case 7:
				content = new EventsMainFragment();
				contentType = ItemType.EVENTS;
				break;
			case 8:
				content = new CompaniesFragment();
				contentType = ItemType.COMPANIES;
				break;
			case 9:
				content = new UsersFragment();
				contentType = ItemType.USERS;
				break;
		}

		if (content == null) {
			if (!User.getInstance().isLogged()) {
				content = new PostsMainFragment();
				contentType = ItemType.POSTS;
			} else {
				content = new FeedMainFragment();
				contentType = ItemType.FEED;
			}
		}
		
		// switchContent(content, contentType);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("currentSection", contentType.ordinal());
	}
	
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private void initNavigationDrawer() {
		ArrayList<MenuData> menu = new ArrayList<MenuData>();

		if (!User.getInstance().isLogged())
			menu.add(new MenuData(R.string.auth, R.drawable.ic_users,
					ItemType.AUTH, false));
		else {
			menu.add(new MenuData(R.string.account, 0, null, true));
			// TODO: set user avatar
			menu.add(new MenuData(User.getInstance().getLogin(),
					R.drawable.ic_users, ItemType.PROFILE, false));
			menu.add(new MenuData(R.string.feed, R.drawable.ic_feed,
					ItemType.FEED, false));
			menu.add(new MenuData(R.string.favorites, R.drawable.ic_favorites,
					ItemType.FAVORITES, false));
		}

		menu.add(new MenuData(R.string.sections, 0, null, true));
		menu.add(new MenuData(R.string.posts, R.drawable.ic_posts,
				ItemType.POSTS, false));
		menu.add(new MenuData(R.string.hubs, R.drawable.ic_hubs, ItemType.HUBS,
				false));
		menu.add(new MenuData(R.string.qa, R.drawable.ic_qa, ItemType.QA, false));
		menu.add(new MenuData(R.string.events, R.drawable.ic_events,
				ItemType.EVENTS, false));
		menu.add(new MenuData(R.string.companies, R.drawable.ic_companies,
				ItemType.COMPANIES, false));
		menu.add(new MenuData(R.string.people, R.drawable.ic_users,
				ItemType.USERS, false));

		MenuAdapter menuAdapter = new MenuAdapter(this, menu);
		
		drawerList = (ListView) findViewById(R.id.navigation_drawer_frame);
		drawerList.setAdapter(menuAdapter);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
		
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_launcher, R.string.app_name, R.string.app_name){
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			}
		};
		
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
	}

	public void switchContent(Fragment fragment,
			MenuFragment.ItemType contentType) {
		content = fragment;
		this.contentType = contentType;

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		if (User.getInstance().isLogged()) {
			MenuInflater menuInflater = getSupportMenuInflater();
			menuInflater.inflate(R.menu.main_activity, menu);
		}

		return true;
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (drawerLayout.isDrawerOpen(drawerList))
				drawerLayout.closeDrawer(drawerList);
			else
				drawerLayout.openDrawer(drawerList);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				return true;
			case R.id.sign_out:
				switchContent(new SignOutFragment(), null);
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private class MenuData {
		public String title;
		public int icon;
		public ItemType itemType;
		public boolean isSection;

		public MenuData(int title, int icon, ItemType itemType,
				boolean isSection) {
			this(getString(title), icon, itemType, isSection);
		}

		public MenuData(String title, int icon, ItemType itemType,
				boolean isSection) {
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
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

				View view = layoutInflater.inflate(R.layout.slide_menu_row,
						null);

				TextView title = (TextView) view
						.findViewById(R.id.slide_menu_title);

				Drawable img = context.getResources().getDrawable(data.icon);
				title.setCompoundDrawablesWithIntrinsicBounds(img, null, null,
						null);

				title.setText(data.title);

				return view;
			} else {
				View view = layoutInflater.inflate(
						R.layout.slide_menu_section_row, null);

				TextView title = (TextView) view
						.findViewById(R.id.slide_menu_title);

				title.setText(data.title);

				return view;
			}
		}

	}

}