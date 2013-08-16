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

package net.meiolania.apps.habrahabr.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;

public class TabListener<T extends Fragment> implements
		com.actionbarsherlock.app.ActionBar.TabListener {
	private Fragment fragment;
	private Class<T> fragmentClass;
	private Context context;
	private String tag;
	private Bundle arguments;

	public TabListener(Context context, String tag, Class<T> fragmentClass) {
		this.context = context;
		this.tag = tag;
		this.fragmentClass = fragmentClass;
	}

	public TabListener(Context context, String tag, Class<T> fragmentClass,
			Bundle arguments) {
		this(context, tag, fragmentClass);
		this.arguments = arguments;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (fragment == null) {
			fragment = Fragment.instantiate(context, fragmentClass.getName());

			if (arguments != null)
				fragment.setArguments(arguments);
		}
		ft.replace(android.R.id.content, fragment, tag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

}