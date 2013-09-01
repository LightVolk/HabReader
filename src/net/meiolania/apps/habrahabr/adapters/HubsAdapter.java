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

import net.meiolania.apps.habrahabr.Fonts;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.hubs.HubEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HubsAdapter extends BaseAdapter {
	private List<HubEntry> hubs;
	private Context context;
	private LayoutInflater layoutInflater;

	public HubsAdapter(Context context, List<HubEntry> hubs) {
		this.context = context;
		this.hubs = hubs;

		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return hubs.size();
	}

	public HubEntry getItem(int position) {
		return hubs.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		HubEntry data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.hubs_list_row, null);

			viewHolder = new ViewHolder();

			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.info = (TextView) view.findViewById(R.id.info);
			viewHolder.index = (TextView) view.findViewById(R.id.index);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		viewHolder.title.setText(data.getTitle());
		viewHolder.title.setTypeface(Fonts.ROBOTO_BOLD);

		viewHolder.index.setText(String.valueOf(data.getIndex()));
		viewHolder.index.setTypeface(Fonts.ROBOTO_BOLD);

		StringBuilder index = new StringBuilder();

		if (data.getMembersCount() != null)
			index.append(data.getMembersCount());

		if (data.getPostsCount() != null) {
			index.append(", ");
			index.append(data.getPostsCount());
		}

		if (data.getQaCounts() != null) {
			index.append(", ");
			index.append(data.getQaCounts());
		}

		viewHolder.info.setText(index.toString());
		viewHolder.info.setTypeface(Fonts.ROBOTO_LIGHT);

		return view;
	}

	static class ViewHolder {
		TextView title;
		TextView info;
		TextView index;
	}

}