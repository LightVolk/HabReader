package net.meiolania.apps.habrahabr.fragments.feed;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;

public class FeedMainFragment extends SherlockFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	showActionBar();
    }

    private void showActionBar() {
	ActionBar actionBar = getSherlockActivity().getSupportActionBar();
	actionBar.removeAllTabs();
	actionBar.setTitle(R.string.feed);
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	/* Posts tab */
	Tab tab = actionBar.newTab();
	tab.setText(R.string.posts);
	tab.setTag("posts");
	tab.setTabListener(new TabListener<FeedPostsFragment>(getSherlockActivity(), "posts", FeedPostsFragment.class));
	actionBar.addTab(tab);

	/* Q&A tab */
	tab = actionBar.newTab();
	tab.setText(R.string.qa);
	tab.setTag("qa");
	tab.setTabListener(new TabListener<FeedQAFragment>(getSherlockActivity(), "qa", FeedQAFragment.class));
	actionBar.addTab(tab);

	/* Events tab */
	tab = actionBar.newTab();
	tab.setText(R.string.events);
	tab.setTag("events");
	tab.setTabListener(new TabListener<FeedEventsFragment>(getSherlockActivity(), "events", FeedEventsFragment.class));
	actionBar.addTab(tab);
    }

}