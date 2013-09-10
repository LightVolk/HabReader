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

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.TabsPagerAdapter;
import net.meiolania.apps.habrahabr.adapters.TabsPagerAdapter.Item;
import net.meiolania.apps.habrahabr.fragments.qa.QaCommentsFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaShowFragment;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;

public class QaShowActivity extends BaseActivity {
	private final static String CURRENT_TAB_KEY = "currentTab";
	private ViewPager viewPager;
	private PagerTabStrip pagerTabStrip;
	private int currentTab;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_show);

		showActionBar();
		initContent();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(CURRENT_TAB_KEY, viewPager.getCurrentItem());
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);

		currentTab = inState.getInt(CURRENT_TAB_KEY, 0);
	}

	private void showActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void initContent() {
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		pagerTabStrip = (PagerTabStrip) findViewById(R.id.tab_strip);

		TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), createFragments());
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(currentTab);
	}

	private List<TabsPagerAdapter.Item> createFragments() {
		List<TabsPagerAdapter.Item> fragments = new ArrayList<TabsPagerAdapter.Item>();

		QaShowFragment qaShowFragment = new QaShowFragment();
		qaShowFragment.setArguments(getIntent().getExtras());

		fragments.add(new Item(getString(R.string.question), qaShowFragment));

		QaCommentsFragment qaCommentsFragment = new QaCommentsFragment();
		qaCommentsFragment.setArguments(getIntent().getExtras());

		fragments.add(new Item(getString(R.string.answers), qaCommentsFragment));

		return fragments;
	}

}