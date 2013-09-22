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
import net.meiolania.apps.habrahabr.api.companies.CompanyEntry;
import net.meiolania.apps.habrahabr.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CompaniesAdapter extends BaseAdapter {
	private List<CompanyEntry> companies;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public CompaniesAdapter(Context context, List<CompanyEntry> companies) {
		this.companies = companies;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.imageLoader.init(ImageUtils.createConfiguration(context));
	}

	public int getCount() {
		return companies.size();
	}

	public CompanyEntry getItem(int position) {
		return companies.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		CompanyEntry data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.companies_list_row, null);

			viewHolder = new ViewHolder();
			
			viewHolder.logo = (ImageView) view.findViewById(R.id.logo);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.index = (TextView) view.findViewById(R.id.index);
			viewHolder.lastPost = (TextView) view.findViewById(R.id.last_post);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		imageLoader.displayImage(data.getLogo(), viewHolder.logo);
		
		viewHolder.name.setText(data.getName());
		viewHolder.name.setTypeface(Fonts.ROBOTO_BOLD);
		
		viewHolder.index.setText(String.valueOf(data.getIndex()));
		viewHolder.index.setTypeface(Fonts.ROBOTO_LIGHT);
		
		if (data.getLastPost() != null) {
			viewHolder.lastPost.setText(data.getLastPost().getTitle());
			viewHolder.lastPost.setTypeface(Fonts.ROBOTO_LIGHT);
		}
		
		return view;
	}

	static class ViewHolder {
		ImageView logo;
		TextView name;
		TextView index;
		TextView lastPost;
	}

}