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

package net.meiolania.apps.habrahabr.fragments.feed;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class FeedMainFragment extends SherlockFragment implements OnNavigationListener {
	public final static int FEED_POSTS = 0;
	public final static int FEED_QA = 1;
	public final static int FEED_EVENTS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

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

		ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(activity, R.array.feed_list,
				R.layout.sherlock_spinner_item);
		listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(listAdapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Fragment fragment = null;

		switch ((int) itemId) {
			case FEED_QA:
				fragment = new FeedQaFragment();
				break;
			case FEED_EVENTS:
				fragment = new FeedEventsFragment();
				break;
			case FEED_POSTS:
			default:
				fragment = new FeedPostsFragment();
				break;
		}

		FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();

		String tag = "feed_" + itemId;
		Fragment foundFragment = fragmentManager.findFragmentByTag(tag);

		if (foundFragment == null) {
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
			fragmentTransaction.commit();
		}

		return true;
	}

}