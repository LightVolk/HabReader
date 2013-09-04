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
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.qa.QaEntry;
import net.meiolania.apps.habrahabr.data.QaData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QaAdapter extends BaseAdapter {
	private List<QaEntry> questions;
	private Context context;
	private LayoutInflater layoutInflater;

	public QaAdapter(Context context, List<QaEntry> questions) {
		this.context = context;
		this.questions = questions;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return questions.size();
	}

	public QaEntry getItem(int position) {
		return questions.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		QaEntry data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.qa_list_row, null);

			viewHolder = new ViewHolder();



			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		

		return view;
	}

	static class ViewHolder {
		
	}

}