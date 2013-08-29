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
import android.content.res.Configuration;
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

public class MainActivity extends AbstractionFragmentActivity {
	private Fragment content;
	private MenuFragment.ItemType contentType;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout drawerLayout;
	private FrameLayout drawerFrame;
	private MenuFragment menuFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ac_main);
		
		initNavigationDrawer();
		initNavigationDrawerFrame();
		
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
		
		switchContent(content, contentType);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("currentSection", contentType.ordinal());
	}
	
	private void initNavigationDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
		
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name){
			private CharSequence actionBarTitle;
			
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				
				getSupportActionBar().setTitle(actionBarTitle);
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				
				actionBarTitle = getSupportActionBar().getTitle();
				getSupportActionBar().setTitle(R.string.app_name);
			}
		};
		
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
	}
	
	private void initNavigationDrawerFrame() {
		drawerFrame = (FrameLayout) findViewById(R.id.navigation_drawer_frame);
		menuFragment = new MenuFragment();
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.navigation_drawer_frame, menuFragment);
		transaction.commit();
	}

	public void switchContent(Fragment fragment,
			MenuFragment.ItemType contentType) {
		content = fragment;
		this.contentType = contentType;

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fragment);
		transaction.commit();
		
		drawerLayout.closeDrawer(drawerFrame);
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
			if (drawerLayout.isDrawerOpen(drawerFrame))
				drawerLayout.closeDrawer(drawerFrame);
			else
				drawerLayout.openDrawer(drawerFrame);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sign_out:
				switchContent(new SignOutFragment(), null);
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}