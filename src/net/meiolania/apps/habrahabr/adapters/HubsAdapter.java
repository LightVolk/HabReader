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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.HubsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HubsAdapter extends BaseAdapter {
    private ArrayList<HubsData> hubs;
    private Context context;

    public HubsAdapter(Context context, ArrayList<HubsData> hubs) {
	this.context = context;
	this.hubs = hubs;
    }

    public int getCount() {
	return hubs.size();
    }

    public HubsData getItem(int position) {
	return hubs.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	HubsData data = getItem(position);

	View view = convertView;
	if (view == null) {
	    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = layoutInflater.inflate(R.layout.hubs_list_row, null);
	}

	TextView title = (TextView) view.findViewById(R.id.hub_title);
	TextView stat = (TextView) view.findViewById(R.id.hub_stat);
	TextView index = (TextView) view.findViewById(R.id.hub_index);

	title.setText(data.getTitle());
	stat.setText(data.getStat());
	index.setText(data.getIndex());

	return view;
    }

}