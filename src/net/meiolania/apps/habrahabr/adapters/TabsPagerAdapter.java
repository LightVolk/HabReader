package net.meiolania.apps.habrahabr.adapters;

import java.util.List;

import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	private List<Item> fragments;
	private float pageWidth;
	
	public TabsPagerAdapter(Context context, FragmentManager fm, List<Item> fragments) {
		super(fm);
		
		this.fragments = fragments;
		
		TypedValue tempValue = new TypedValue();
		context.getResources().getValue(R.dimen.view_pager_page_width, tempValue, true);
		
		pageWidth = tempValue.getFloat();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).getTitle();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position).getFragment();
	}
	
	@Override
	public float getPageWidth(int position) {
		return pageWidth;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	public static class Item {
		private String title;
		private Fragment fragment;

		public Item(String title, Fragment fragment) {
			this.title = title;
			this.fragment = fragment;
		}

		public String getTitle() {
			return title;
		}

		public Fragment getFragment() {
			return fragment;
		}

	}

}