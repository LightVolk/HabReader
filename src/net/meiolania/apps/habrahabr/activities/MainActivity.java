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
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends AbstractionFragmentActivity {
    public static final String CONTENT_EXTRAS = "content";
    private Fragment content;
    private MenuFragment.ItemType contentType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	int currentSection = -1;
	if (savedInstanceState != null)
	    currentSection = savedInstanceState.getInt("currentSection");

	if (getIntent().getExtras() != null)
	    currentSection = getIntent().getExtras().getInt(CONTENT_EXTRAS);

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
	    content = new PostsMainFragment();
	    contentType = ItemType.POSTS;
	}

	getSupportFragmentManager().beginTransaction().replace(android.R.id.content, content).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);

	outState.putInt("currentSection", contentType.ordinal());
    }

    public void switchContent(Fragment fragment, MenuFragment.ItemType contentType) {
	content = fragment;
	this.contentType = contentType;

	getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

	toggle();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	
	MenuInflater menuInflater = getSupportMenuInflater();
	menuInflater.inflate(R.menu.main_activity, menu);
	
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	switch (item.getItemId()) {
	    case android.R.id.home:
		toggle();
		return true;
	    case R.id.sign_out:
		switchContent(new SignOutFragment(), null);
		return true;
	}
	return super.onMenuItemSelected(featureId, item);
    }

    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	};
    }

}