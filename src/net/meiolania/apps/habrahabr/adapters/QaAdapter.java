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
import net.meiolania.apps.habrahabr.data.QaData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QaAdapter extends BaseAdapter {
    private ArrayList<QaData> questions;
    private Context context;
    private boolean additionalLayout = false;

    public QaAdapter(Context context, ArrayList<QaData> questions) {
	this.context = context;
	this.questions = questions;

	Preferences preferences = Preferences.getInstance(context);
	this.additionalLayout = preferences.getAdditionalQa();
    }

    public int getCount() {
	return questions.size();
    }

    public QaData getItem(int position) {
	return questions.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	QaData data = getItem(position);

	if (additionalLayout) {
	    View view = convertView;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.qa_list_row, null);
	    }

	    TextView title = (TextView) view.findViewById(R.id.qa_title);
	    title.setText(data.getTitle());

	    TextView hubs = (TextView) view.findViewById(R.id.qa_hubs);
	    TextView author = (TextView) view.findViewById(R.id.qa_author);
	    TextView date = (TextView) view.findViewById(R.id.qa_date);
	    TextView answers = (TextView) view.findViewById(R.id.qa_answers);

	    hubs.setText(data.getHubs());
	    author.setText(data.getAuthor());
	    date.setText(data.getDate());
	    answers.setText(data.getAnswers());

	    return view;
	} else {
	    View view = convertView;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.qa_list_row_simple, null);
	    }
	    
	    TextView title = (TextView) view.findViewById(R.id.qa_title);
	    title.setText(data.getTitle());
	    
	    return view;
	}
    }

}