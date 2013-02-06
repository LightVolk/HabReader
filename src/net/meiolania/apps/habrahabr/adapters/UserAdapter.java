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
import net.meiolania.apps.habrahabr.data.UsersData;
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

public class UserAdapter extends BaseAdapter {
    private ArrayList<UsersData> people;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public UserAdapter(Context context, ArrayList<UsersData> people) {
	this.context = context;
	this.people = people;

	DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
	ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheSize(3000000)
		.discCacheSize(50000000).httpReadTimeout(5000).defaultDisplayImageOptions(options).build();
	this.imageLoader.init(configuration);
    }

    public int getCount() {
	return people.size();
    }

    public UsersData getItem(int position) {
	return people.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	UsersData data = getItem(position);

	View view = convertView;
	if (view == null) {
	    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = layoutInflater.inflate(R.layout.users_list_row, null);
	}

	TextView title = (TextView) view.findViewById(R.id.people_title);
	title.setText(data.getName());

	ImageView avatar = (ImageView) view.findViewById(R.id.people_avatar);
	imageLoader.displayImage(data.getAvatar(), avatar);

	TextView karma = (TextView) view.findViewById(R.id.people_karma);
	karma.setText(context.getString(R.string.karma_count).replace("%d", data.getKarma()));

	TextView rating = (TextView) view.findViewById(R.id.people_rating);
	rating.setText(context.getString(R.string.rating_count).replace("%d", data.getRating()));

	return view;
    }

}