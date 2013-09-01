package net.meiolania.apps.habrahabr.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	private List<Item> fragments;
	
	public TabsPagerAdapter(FragmentManager fm, List<Item> fragments) {
		super(fm);
		
		this.fragments = fragments;
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