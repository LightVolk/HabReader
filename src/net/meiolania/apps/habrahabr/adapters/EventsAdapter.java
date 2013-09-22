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
import net.meiolania.apps.habrahabr.api.events.EventEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter {
	private List<EventEntry> events;
	private LayoutInflater layoutInflater;

	public EventsAdapter(Context context, List<EventEntry> events) {
		this.events = events;

		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return events.size();
	}

	public EventEntry getItem(int position) {
		return events.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		EventEntry data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {

			view = layoutInflater.inflate(R.layout.events_list_row, null);

			viewHolder = new ViewHolder();

			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.hub = (TextView) view.findViewById(R.id.hub);
			viewHolder.day = (TextView) view.findViewById(R.id.day);
			viewHolder.month = (TextView) view.findViewById(R.id.month);
			viewHolder.text = (TextView) view.findViewById(R.id.text);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		viewHolder.title.setText(data.getTitle());
		viewHolder.title.setTypeface(Fonts.ROBOTO_BOLD);

		viewHolder.hub.setText(data.getHubs().get(0).getTitle());
		viewHolder.hub.setTypeface(Fonts.ROBOTO_LIGHT);

		viewHolder.day.setText(String.valueOf(data.getDay()));
		viewHolder.day.setTypeface(Fonts.ROBOTO_LIGHT);

		viewHolder.month.setText(data.getMonth());
		viewHolder.month.setTypeface(Fonts.ROBOTO_LIGHT);

		viewHolder.text.setText(data.getText());
		viewHolder.text.setTypeface(Fonts.ROBOTO_REGULAR);

		return view;
	}

	static class ViewHolder {
		TextView title;
		TextView hub;
		TextView day;
		TextView month;
		TextView text;
	}

}