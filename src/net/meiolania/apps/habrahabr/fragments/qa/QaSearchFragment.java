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

package net.meiolania.apps.habrahabr.fragments.qa;

import java.util.List;

import net.meiolania.apps.habrahabr.HabrAuthApi;
import net.meiolania.apps.habrahabr.api.qa.QaApi;
import net.meiolania.apps.habrahabr.api.qa.QaEntry;
import android.os.Bundle;

public class QaSearchFragment extends QaFragment {
	public final static String EXTRA_QUERY = "query";
	private String query;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		query = getArguments().getString(EXTRA_QUERY);
	}

	@Override
	public List<QaEntry> getQuestion(int page) {
		QaApi qaApi = new QaApi(HabrAuthApi.getInstance());
		return qaApi.searchQuestions(page, query);
	}

}