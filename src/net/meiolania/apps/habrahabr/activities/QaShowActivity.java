/*
Copyright 2012 Andrey Zaytsev

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
import net.meiolania.apps.habrahabr.fragments.qa.QaCommentsFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaShowFragment;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

public class QaShowActivity extends AbstractionFragmentActivity {
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_TITLE = "title";
    private String title;
    private String url;
    private int currentTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	if (savedInstanceState != null)
	    currentTab = savedInstanceState.getInt("currentTab");

	loadExtras();
	showActionBar();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putInt("currentTab", getSupportActionBar().getSelectedTab().getPosition());
    }

    private void loadExtras() {
	Uri data = getIntent().getData();
	if (data != null)
	    url = data.toString();
	else
	    url = getIntent().getStringExtra(EXTRA_URL);
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(title);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	/*
	 * Question tab
	 */
	Bundle arguments = new Bundle();
	arguments.putString(QaShowFragment.URL_ARGUMENT, url);

	Tab tab = actionBar.newTab().setText(R.string.question).setTag("question")
		.setTabListener(new TabListener<QaShowFragment>(this, "question", QaShowFragment.class, arguments));
	actionBar.addTab(tab, (currentTab == 0) ? true : false);

	/*
	 * Comments tab
	 */
	arguments = new Bundle();
	arguments.putString(QaCommentsFragment.URL_ARGUMENT, url);

	tab = actionBar.newTab().setText(R.string.comments).setTag("comments")
		.setTabListener(new TabListener<QaCommentsFragment>(this, "comments", QaCommentsFragment.class, arguments));
	actionBar.addTab(tab, (currentTab == 1) ? true : false);
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