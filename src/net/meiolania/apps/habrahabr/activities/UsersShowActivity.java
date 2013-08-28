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

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.fragments.users.UsersShowFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;

public class UsersShowActivity extends AbstractionFragmentActivity {
	public final static String EXTRA_NAME = "name";
	public final static String EXTRA_URL = "url";
	private String name;
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadExtras();
		showActionBar();
		loadInfo();
	}

	private void loadExtras() {
		Uri habraUrl = getIntent().getData();
		if (habraUrl != null)
			url = habraUrl.toString();
		else
			url = getIntent().getStringExtra(EXTRA_URL);
		name = getIntent().getStringExtra(EXTRA_NAME);
	}

	private void showActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(name);
	}

	private void loadInfo() {
		UsersShowFragment fragment = new UsersShowFragment();

		Bundle arguments = new Bundle();
		arguments.putString(UsersShowFragment.URL_ARGUMENT, url);

		fragment.setArguments(arguments);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(android.R.id.content, fragment);
		fragmentTransaction.commit();
	}

}