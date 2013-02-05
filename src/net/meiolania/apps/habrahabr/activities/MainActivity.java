package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.auth.AuthFragment;
import net.meiolania.apps.habrahabr.auth.SignOutFragment;
import net.meiolania.apps.habrahabr.fragments.companies.CompaniesFragment;
import net.meiolania.apps.habrahabr.fragments.events.EventsMainFragment;
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

import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends AbstractionFragmentActivity {
    private Fragment content;
    private MenuFragment.ItemType contentType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	if (savedInstanceState != null) {
	    // @TODO: think of something new.
	    int currentSection = savedInstanceState.getInt("currentSection");
	    switch (currentSection) {
		case 0:
		    content = new AuthFragment();
		    contentType = ItemType.AUTH;
		    break;
		case 1:

		    break;
		case 2:
		    content = new SignOutFragment();
		    contentType = ItemType.SIGN_OUT;
		    break;
		case 3:
		    content = new FeedMainFragment();
		    contentType = ItemType.FEED;
		    break;
		case 4:

		    break;
		case 5:
		    content = new PostsMainFragment();
		    contentType = ItemType.POSTS;
		    break;
		case 6:
		    content = new HubsMainFragment();
		    contentType = ItemType.HUBS;
		    break;
		case 7:
		    content = new QaMainFragment();
		    contentType = ItemType.QA;
		    break;
		case 8:
		    content = new EventsMainFragment();
		    contentType = ItemType.EVENTS;
		    break;
		case 9:
		    content = new CompaniesFragment();
		    contentType = ItemType.COMPANIES;
		    break;
		case 10:
		    content = new UsersFragment();
		    contentType = ItemType.USERS;
		    break;
	    }
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
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	switch (item.getItemId()) {
	    case android.R.id.home:
		toggle();
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