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
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter {
    private ArrayList<EventsData> events;
    private Context context;
    private boolean additionalLayout = false;

    public EventsAdapter(Context context, ArrayList<EventsData> events) {
	this.context = context;
	this.events = events;

	Preferences preferences = Preferences.getInstance(context);
	this.additionalLayout = preferences.getAdditionalEvents();
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

    public View getView(int position, View convertView, ViewGroup parent) {
	EventsData data = getItem(position);

	View view = convertView;
	if (view == null) {
	    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = layoutInflater.inflate(R.layout.events_list_row, null);
	}

	TextView title = (TextView) view.findViewById(R.id.event_title);
	title.setText(data.getTitle());

	TextView text = (TextView) view.findViewById(R.id.event_text);
	TextView date = (TextView) view.findViewById(R.id.event_date);
	TextView hubs = (TextView) view.findViewById(R.id.event_hubs);

	LinearLayout info = (LinearLayout) view.findViewById(R.id.event_info);

	if (additionalLayout) {
	    text.setText(data.getText());
	    date.setText(data.getDate());
	    hubs.setText(data.getHubs());
	} else {
	    text.setVisibility(View.GONE);
	    info.setVisibility(View.GONE);
	}

	return view;
    }

}