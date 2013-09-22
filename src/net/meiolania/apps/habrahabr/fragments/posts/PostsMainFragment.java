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

package net.meiolania.apps.habrahabr.fragments.posts;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class PostsMainFragment extends SherlockFragment implements OnNavigationListener {
	public final static int POSTS_TOP = 0;
	public final static int POSTS_COLLECTIVE = 1;
	public final static int POSTS_CORPORATIVE = 2;
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		showActionBar();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_main, container, false);
	}

	private void showActionBar() {
		SherlockFragmentActivity activity = getSherlockActivity();

		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.removeAllTabs();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		int selectedTab = Preferences.getInstance(activity).getPostsDefaultTab();
		
		ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(activity, R.array.posts_list, R.layout.sherlock_spinner_item);
		listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(listAdapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		PostsFragment fragment = null;
		
		switch ((int) itemId) {
			case POSTS_COLLECTIVE:
				fragment = new PostsCollectiveFragment();
				break;
			case POSTS_CORPORATIVE:
				fragment = new PostsCorporativeFragment();
				break;
			case POSTS_TOP:
			default:
				fragment = new PostsTopFragment();
				break;
		}
		
		FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
		
		return true;
	}
}