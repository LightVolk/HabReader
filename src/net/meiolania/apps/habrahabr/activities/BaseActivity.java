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

import net.meiolania.apps.habrahabr.Fonts;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.HabrAuthApi;
import net.meiolania.apps.habrahabr.auth.User;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActivity extends SherlockFragmentActivity {
	public final static String DEVELOPER_PLAY_LINK = "https://play.google.com/store/apps/developer?id=Andrey+Zaytsev";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		initKeepScreenOn();

		// Auth
		User.getInstance().init(this);
		HabrAuthApi.getInstance().init(this);

		// UI
		Fonts.init(this);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);
	}

	@Override
	protected void onResume() {
		initKeepScreenOn();

		super.onResume();
	}

	private void initKeepScreenOn() {
		if (Preferences.getInstance(this).getKeepScreen())
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.global_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onHomeClick();
				return true;
			case R.id.preferences:
				onPreferencesClick();
				return true;
			case R.id.more_applications:
				onApplicationsClick();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void onHomeClick() {
		Intent intent = NavUtils.getParentActivityIntent(this);
		if (NavUtils.shouldUpRecreateTask(this, intent)) {
			TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
			taskStackBuilder.addNextIntentWithParentStack(intent);
			taskStackBuilder.startActivities();
		} else
			NavUtils.navigateUpTo(this, intent);
	}

	private void onPreferencesClick() {
		startActivity(new Intent(this, PreferencesActivity.class));
	}

	private void onApplicationsClick() {
		Uri uri = Uri.parse(DEVELOPER_PLAY_LINK);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

}