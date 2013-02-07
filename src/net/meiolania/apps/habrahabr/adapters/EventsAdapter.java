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

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.EventsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter {
    private ArrayList<EventsData> events;
    private Context context;
    private boolean additionalLayout = false;

    public EventsAdapter(Context context, ArrayList<EventsData> events) {
	this.context = context;
	this.events = events;

	additionalLayout = Preferences.getInstance(context).getAdditionalEvents();
    }

    public int getCount() {
	return events.size();
    }

    public EventsData getItem(int position) {
	return events.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
	EventsData data = getItem(position);

	if (additionalLayout) {
	    ViewHolder viewHolder;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.events_list_row, null);

		viewHolder = new ViewHolder();

		viewHolder.title = (TextView) view.findViewById(R.id.event_title);
		viewHolder.text = (TextView) view.findViewById(R.id.event_text);
		viewHolder.date = (TextView) view.findViewById(R.id.event_date);
		viewHolder.hubs = (TextView) view.findViewById(R.id.event_hubs);

		view.setTag(viewHolder);
	    } else
		viewHolder = (ViewHolder) view.getTag();

	    viewHolder.title.setText(data.getTitle());
	    viewHolder.text.setText(data.getText());
	    viewHolder.date.setText(data.getDate());
	    viewHolder.hubs.setText(data.getHubs());

	    return view;
	} else {
	    ViewHolder viewHolder;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.events_list_row_simple, null);

		viewHolder = new ViewHolder();

		viewHolder.title = (TextView) view.findViewById(R.id.event_title);

		view.setTag(viewHolder);
	    } else
		viewHolder = (ViewHolder) view.getTag();

	    viewHolder.title.setText(data.getTitle());

	    return view;
	}
    }

    static class ViewHolder {
	TextView title;
	TextView text;
	TextView date;
	TextView hubs;
    }

}