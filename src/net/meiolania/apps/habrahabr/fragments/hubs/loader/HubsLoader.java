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

package net.meiolania.apps.habrahabr.fragments.hubs.loader;

import java.util.List;

import net.meiolania.apps.habrahabr.HabrAuthApi;
import net.meiolania.apps.habrahabr.api.hubs.HubsApi;
import net.meiolania.apps.habrahabr.api.hubs.HubEntry;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class HubsLoader extends AsyncTaskLoader<List<HubEntry>> {
	private String url;
	private int page;

	public HubsLoader(Context context, String url, int page) {
		super(context);
		this.url = url;
		this.page = page;
	}

	@Override
	public List<HubEntry> loadInBackground() {
		HubsApi hubsApi = new HubsApi(HabrAuthApi.getInstance());
		return hubsApi.getHubs(url, page);
	}

}