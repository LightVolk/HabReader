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

package net.meiolania.apps.habrahabr.fragments.qa.loader;

import java.util.List;

import net.meiolania.apps.habrahabr.api.qa.QaEntry;
import net.meiolania.apps.habrahabr.fragments.qa.QaFragment;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class QaLoader extends AsyncTaskLoader<List<QaEntry>> {
	private QaFragment fragment;
	private int page;

	public QaLoader(Context context, QaFragment fragment, int page) {
		super(context);
		this.fragment = fragment;
		this.page = page;
	}

	@Override
	public List<QaEntry> loadInBackground() {
		return fragment.getQuestion(page);
	}

}