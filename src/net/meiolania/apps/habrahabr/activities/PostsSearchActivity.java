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

import com.actionbarsherlock.app.ActionBar;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.posts.PostsSearchFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class PostsSearchActivity extends AbstractionFragmentActivity {
    public final static String EXTRA_QUERY = "query";
    private String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	loadExtras();
	showActionBar();
	loadSearchedPosts();
    }
    
    private void loadExtras() {
	query = getIntent().getStringExtra(EXTRA_QUERY);
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setTitle(R.string.post_search);
	actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadSearchedPosts() {
	PostsSearchFragment fragment = new PostsSearchFragment(query);

	FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
	fragmentTransaction.replace(android.R.id.content, fragment);
	fragmentTransaction.commit();
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