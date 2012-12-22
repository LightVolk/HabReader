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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.posts.PostShowFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsCommentsFragment;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;

public class PostsShowActivity extends AbstractionActivity
{
	public final static String EXTRA_URL = "url";
	public final static String EXTRA_TITLE = "title";
	private String url;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		loadExtras();
		showActionBar();
	}

	private void loadExtras()
	{
		url = getIntent().getStringExtra(EXTRA_URL);
		title = getIntent().getStringExtra(EXTRA_TITLE);
	}

	private void showActionBar()
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(title);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		/*
		 * Post tab
		 */
		Bundle arguments = new Bundle();
		arguments.putString(PostShowFragment.URL_ARGUMENT, url);

		Tab tab = actionBar.newTab()
						   .setText(R.string.post)
						   .setTag("post")
						   .setTabListener(new TabListener<PostShowFragment>(this, "post", PostShowFragment.class, arguments));
		actionBar.addTab(tab);

		/*
		 * Comments tab
		 */
		arguments = new Bundle();
		arguments.putString(PostsCommentsFragment.URL_ARGUMENT, url);

		tab = actionBar.newTab()
					   .setText(R.string.comments)
					   .setTag("comments")
					   .setTabListener(new TabListener<PostsCommentsFragment>(this, "comments", PostsCommentsFragment.class, arguments));
		actionBar.addTab(tab);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(this, PostsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected OnClickListener getConnectionDialogListener()
	{
		return new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		};
	}

}