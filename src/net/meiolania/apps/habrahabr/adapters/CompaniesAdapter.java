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
import net.meiolania.apps.habrahabr.data.CompaniesData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CompaniesAdapter extends BaseAdapter {
    private ArrayList<CompaniesData> companies;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public CompaniesAdapter(Context context, ArrayList<CompaniesData> companies) {
	this.context = context;
	this.companies = companies;

	DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
	ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheSize(3000000)
		.discCacheSize(50000000).httpReadTimeout(5000).defaultDisplayImageOptions(options).build();
	this.imageLoader.init(configuration);
    }

    public int getCount() {
	return companies.size();
    }

    public CompaniesData getItem(int position) {
	return companies.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
	CompaniesData data = getItem(position);

	ViewHolder viewHolder;
	if (view == null) {
	    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = layoutInflater.inflate(R.layout.companies_list_row, null);
	    
	    viewHolder = new ViewHolder();
	    
	    viewHolder.title = (TextView) view.findViewById(R.id.company_title);
	    viewHolder.icon = (ImageView) view.findViewById(R.id.company_icon);
	    viewHolder.index = (TextView) view.findViewById(R.id.company_index);
	    
	    view.setTag(viewHolder);
	} else
	    viewHolder = (ViewHolder) view.getTag();

	viewHolder.title.setText(data.getTitle());
	imageLoader.displayImage(data.getIcon(), viewHolder.icon);
	viewHolder.index.setText(String.format(context.getString(R.string.habraindex), data.getIndex()));

	return view;
    }
    
    static class ViewHolder {
	TextView title;
	ImageView icon;
	TextView index;
    }

}