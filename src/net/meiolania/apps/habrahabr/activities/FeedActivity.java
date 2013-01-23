package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.feed.FeedEventsFragment;
import net.meiolania.apps.habrahabr.fragments.feed.FeedPostsFragment;
import net.meiolania.apps.habrahabr.fragments.feed.FeedQAFragment;
import net.meiolania.apps.habrahabr.ui.TabListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class FeedActivity extends AbstractionFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        showActionBar();
    }
    
    private void showActionBar(){
	ActionBar actionBar = getSupportActionBar();
	actionBar.setTitle(R.string.feed);
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
	/* Posts tab */
	Tab tab = actionBar.newTab();
	tab.setText(R.string.posts);
	tab.setTag("posts");
	tab.setTabListener(new TabListener<FeedPostsFragment>(this, "posts", FeedPostsFragment.class));
	actionBar.addTab(tab);
	
	/* Q&A tab */
	tab = actionBar.newTab();
	tab.setText(R.string.qa);
	tab.setTag("qa");
	tab.setTabListener(new TabListener<FeedQAFragment>(this, "qa", FeedQAFragment.class));
	actionBar.addTab(tab);
	
	/* Events tab */
	tab = actionBar.newTab();
	tab.setText(R.string.events);
	tab.setTag("events");
	tab.setTabListener(new TabListener<FeedEventsFragment>(this, "events", FeedEventsFragment.class));
	actionBar.addTab(tab);
    }
    
    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		finish();
	    }
	};
    }

}