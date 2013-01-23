package net.meiolania.apps.habrahabr.fragments.events;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.MainTabListener;
import android.os.Bundle;
import android.os.Handler;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

public class EventsMainFragment extends SherlockFragment {

    public static EventsMainFragment newInstance() {
	return new EventsMainFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	// It's not good
	// TODO: Think about that
	Handler h = new Handler();
	h.postDelayed(new Runnable() {
	    public void run() {
		showActionBar();
	    }
	}, 300);
    }

    private void showActionBar() {
	SherlockFragmentActivity activity = getSherlockActivity();

	ActionBar actionBar = activity.getSupportActionBar();
	actionBar.removeAllTabs();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(R.string.events);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	Preferences preferences = Preferences.getInstance(activity);
	int selectedTab = preferences.getEventsDefaultTab();

	/* Coming tab */
	Tab tab = actionBar.newTab();
	tab.setText(R.string.coming);
	tab.setTag("coming");
	tab.setTabListener(new MainTabListener<EventComingFragment>(activity, "coming", EventComingFragment.class));
	actionBar.addTab(tab, (selectedTab == 0 ? true : false));

	/* Current tab */
	tab = actionBar.newTab();
	tab.setText(R.string.current);
	tab.setTag("current");
	tab.setTabListener(new MainTabListener<EventCurrentFragment>(activity, "current", EventCurrentFragment.class));
	actionBar.addTab(tab, (selectedTab == 1 ? true : false));

	/* Past tab */
	tab = actionBar.newTab();
	tab.setText(R.string.past);
	tab.setTag("past");
	tab.setTabListener(new MainTabListener<EventPastFragment>(activity, "past", EventPastFragment.class));
	actionBar.addTab(tab, (selectedTab == 2 ? true : false));
    }
}