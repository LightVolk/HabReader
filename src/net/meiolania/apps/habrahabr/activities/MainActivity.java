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

import net.meiolania.apps.habrahabr.HabrAuthApi;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.auth.AuthFragment;
import net.meiolania.apps.habrahabr.fragments.SideMenuFragment;
import net.meiolania.apps.habrahabr.fragments.SideMenuFragment.ItemType;
import net.meiolania.apps.habrahabr.fragments.companies.CompaniesFragment;
import net.meiolania.apps.habrahabr.fragments.conversations.ConversationsFragment;
import net.meiolania.apps.habrahabr.fragments.events.EventsMainFragment;
import net.meiolania.apps.habrahabr.fragments.favorites.FavoritesMainFragment;
import net.meiolania.apps.habrahabr.fragments.feed.FeedMainFragment;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsMainFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaMainFragment;
import net.meiolania.apps.habrahabr.fragments.users.UsersFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends BaseActivity {
	public final static String CURRENT_SECTION_KEY = "currentSection";
	public final static String DEVELOPER_PLAY_LINK = "https://play.google.com/store/apps/developer?id=Andrey+Zaytsev";
	private SideMenuFragment.ItemType contentType;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout drawerLayout;
	private FrameLayout drawerFrame;
	private SideMenuFragment menuFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_main);

		initNavigationDrawer();
		initNavigationDrawerFrame();

		initContent();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(CURRENT_SECTION_KEY, contentType);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater menuInflater = getSupportMenuInflater();

		menuInflater.inflate(R.menu.ac_main, menu);

		if (HabrAuthApi.getInstance().isAuth())
			menuInflater.inflate(R.menu.user, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onHomeClick();
				return true;
			case R.id.preferences:
				onPreferencesClick();
				return true;
			case R.id.more_applications:
				onApplicationsClick();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initContent() {
		Bundle extras = getIntent().getExtras();

		ItemType itemType = ItemType.POSTS;
		if (extras != null)
			itemType = (ItemType) extras.getSerializable(CURRENT_SECTION_KEY);
		else if (HabrAuthApi.getInstance().isAuth())
			itemType = ItemType.FEED;

		switchContent(itemType);
	}

	private void initNavigationDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		drawerLayout.setDrawerListener(actionBarDrawerToggle);
	}

	private void initNavigationDrawerFrame() {
		drawerFrame = (FrameLayout) findViewById(R.id.navigation_drawer_frame);
		menuFragment = new SideMenuFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.navigation_drawer_frame, menuFragment);
		transaction.commit();
	}

	public void switchContent(Fragment fragment, ItemType itemType) {
		this.contentType = itemType;

		Fragment foundFragment = getSupportFragmentManager().findFragmentByTag(itemType.name());

		if (foundFragment == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.content_frame, fragment, itemType.name());
			transaction.commit();
		}

		drawerLayout.closeDrawer(drawerFrame);
	}

	public void switchContent(ItemType itemType) {
		switchContent(getFragment(itemType), itemType);
	}

	private Fragment getFragment(ItemType itemType) {
		Fragment fragment = null;

		if (itemType != null) {
			switch (itemType) {
				case AUTH:
					fragment = new AuthFragment();
					break;
				case FEED:
					fragment = new FeedMainFragment();
					break;
				case FAVORITES:
					fragment = new FavoritesMainFragment();
					break;
				case CONVERSATIONS:
					fragment = new ConversationsFragment();
					break;
				case POSTS:
					fragment = new PostsMainFragment();
					break;
				case HUBS:
					fragment = new HubsMainFragment();
					break;
				case QA:
					fragment = new QaMainFragment();
					break;
				case EVENTS:
					fragment = new EventsMainFragment();
					break;
				case COMPANIES:
					fragment = new CompaniesFragment();
					break;
				case USERS:
					fragment = new UsersFragment();
					break;
				case PROFILE:
					Intent intent = new Intent(this, UsersShowActivity.class);
					intent.putExtra(UsersShowActivity.EXTRA_URL, "http://habrahabr.ru/users/" + HabrAuthApi.getInstance().getLogin());
					startActivity(intent);
					break;
			}
		}

		return fragment;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		actionBarDrawerToggle.syncState();
	}

	private void onHomeClick() {
		if (drawerLayout.isDrawerOpen(drawerFrame))
			drawerLayout.closeDrawer(drawerFrame);
		else
			drawerLayout.openDrawer(drawerFrame);
	}

	private void onPreferencesClick() {
		startActivity(new Intent(this, PreferencesActivity.class));
	}

	private void onApplicationsClick() {
		Uri uri = Uri.parse(DEVELOPER_PLAY_LINK);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);

		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
		}
	}

}