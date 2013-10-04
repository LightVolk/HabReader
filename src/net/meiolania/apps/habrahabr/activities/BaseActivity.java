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
import net.meiolania.apps.habrahabr.HabrAuthApi;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActivity extends SherlockFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		initKeepScreenOn();
		
		getSupportActionBar().setIcon(R.drawable.ic_navigation_drawer);
		getSupportActionBar().setLogo(R.drawable.ic_navigation_drawer);

		// Auth
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onHomeClick();
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

}