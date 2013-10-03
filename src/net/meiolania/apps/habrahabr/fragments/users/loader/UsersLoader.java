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

package net.meiolania.apps.habrahabr.fragments.users.loader;

import java.util.List;

import net.meiolania.apps.habrahabr.api.users.UserEntry;
import net.meiolania.apps.habrahabr.api.users.UsersApi;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class UsersLoader extends AsyncTaskLoader<List<UserEntry>> {
	private String url;

	public UsersLoader(Context context) {
		super(context);
	}

	public UsersLoader(Context context, String url) {
		super(context);

		this.url = url;
	}

	@Override
	public List<UserEntry> loadInBackground() {
		UsersApi usersApi = new UsersApi();

		if (url != null)
			return usersApi.getUsers(url);

		return usersApi.getUsers();
	}

}