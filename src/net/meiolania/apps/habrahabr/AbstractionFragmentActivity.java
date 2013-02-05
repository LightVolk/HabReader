/*
Copyright 2012 Andrey Zaytsev, Sergey Ivanov

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

package net.meiolania.apps.habrahabr;

import net.meiolania.apps.habrahabr.activities.PreferencesActivity;
import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.slidemenu.MenuFragment;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class AbstractionFragmentActivity extends SlidingFragmentActivity {
    public final static String DEVELOPER_PLAY_LINK = "https://play.google.com/store/apps/developer?id=Andrey+Zaytsev";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

	initFullscreen();
	initKeepScreenOn();

	// Auth
	User.getInstance().init(this);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	setSupportProgressBarIndeterminateVisibility(false);
	
	// Slide menu
	setContentView(R.layout.empty_for_slidemenu);
	
	setBehindContentView(R.layout.slide_menu);
	getSupportFragmentManager().beginTransaction().replace(R.id.slide_menu, new MenuFragment()).commit();

	SlidingMenu slidingMenu = getSlidingMenu();
	slidingMenu.setMode(SlidingMenu.LEFT);
	slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	
	// No connection dialog
	// @TODO: rewrite
	if (!ConnectionUtils.isConnected(this)) {
	    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setTitle(R.string.error);
	    dialog.setMessage(getString(R.string.no_connection));
	    dialog.setPositiveButton(R.string.close, getConnectionDialogListener());
	    dialog.setCancelable(false);
	    dialog.show();
	}
    }

    @Override
    protected void onResume() {
	initFullscreen();
	initKeepScreenOn();

	super.onResume();
    }

    private void initKeepScreenOn() {
	if (Preferences.getInstance(this).getKeepScreen())
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	else
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    private void initFullscreen() {
	if (Preferences.getInstance(this).getFullScreen())
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	else
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater menuInflater = getSupportMenuInflater();
	menuInflater.inflate(R.menu.global_activity, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case android.R.id.home:
		finish();
		return true;
	    case R.id.preferences:
		startActivity(new Intent(this, PreferencesActivity.class));
		return true;
	    case R.id.more_applications:
		Uri uri = Uri.parse(DEVELOPER_PLAY_LINK);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		return true;
	}
	return super.onOptionsItemSelected(item);
    }

    protected abstract OnClickListener getConnectionDialogListener();

}