package net.meiolania.apps.habrahabr.fragments.qa;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class QaMainFragment extends SherlockFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	
	showActionBar();
    }

    private void showActionBar() {
	SherlockFragmentActivity activity = getSherlockActivity();

	ActionBar actionBar = activity.getSupportActionBar();
	actionBar.removeAllTabs();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(R.string.qa);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	int selectedTab = Preferences.getInstance(activity).getQaDefaultTab();

	/* Inbox tab */
	Tab tab = actionBar.newTab();
	tab.setText(R.string.inbox);
	tab.setTag("inbox");
	tab.setTabListener(new TabListener<QaInboxFragment>(activity, "inbox", QaInboxFragment.class));
	actionBar.addTab(tab, selectedTab == 0 ? true : false);

	/* Hot tab */
	tab = actionBar.newTab();
	tab.setText(R.string.hot);
	tab.setTag("hot");
	tab.setTabListener(new TabListener<QaHotFragment>(activity, "hot", QaHotFragment.class));
	actionBar.addTab(tab, selectedTab == 1 ? true : false);

	/* Unanswered tab */
	tab = actionBar.newTab();
	tab.setText(R.string.unanswered);
	tab.setTag("unanswered");
	tab.setTabListener(new TabListener<QaUnansweredFragment>(activity, "unanswered", QaUnansweredFragment.class));
	actionBar.addTab(tab, selectedTab == 2 ? true : false);
    }
}