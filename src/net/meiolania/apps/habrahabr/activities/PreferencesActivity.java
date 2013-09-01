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

import net.meiolania.apps.habrahabr.R;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class PreferencesActivity extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		
		showActionBar();
		initPreferences();
	}

	private void showActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.preferences);
	}
	
	@SuppressWarnings("deprecation")
	private void initPreferences() {
		Preference version = (Preference) findPreference("version");
		
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
			version.setSummary(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			version.setSummary(R.string.undefined_value);
		}
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