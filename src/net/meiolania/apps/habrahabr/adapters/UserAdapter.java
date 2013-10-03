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

package net.meiolania.apps.habrahabr.adapters;

import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.users.UserEntry;
import net.meiolania.apps.habrahabr.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class UserAdapter extends BaseAdapter {
	private List<UserEntry> users;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public UserAdapter(Context context, List<UserEntry> users) {
		this.users = users;

		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader.init(ImageUtils.createConfiguration(context));
	}

	public int getCount() {
		return users.size();
	}

	public UserEntry getItem(int position) {
		return users.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		UserEntry data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.users_list_row, null);

			viewHolder = new ViewHolder();

			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar);
			viewHolder.login = (TextView) view.findViewById(R.id.login);
			viewHolder.karma = (TextView) view.findViewById(R.id.karma);
			viewHolder.rating = (TextView) view.findViewById(R.id.rating);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		viewHolder.login.setText(data.getLogin());
		viewHolder.karma.setText(data.getKarma());
		viewHolder.rating.setText(data.getRating());

		imageLoader.displayImage(data.getAvatar(), viewHolder.avatar);

		return view;
	}

	static class ViewHolder {
		ImageView avatar;
		TextView login;
		TextView karma;
		TextView rating;
	}

}