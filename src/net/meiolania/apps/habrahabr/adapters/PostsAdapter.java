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
import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PostsAdapter extends BaseAdapter {
	private AuthApi authApi;
	private List<PostEntry> posts;
	private Context context;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public PostsAdapter(Context context, AuthApi authApi, List<PostEntry> posts) {
		this.context = context;
		this.authApi = authApi;
		this.posts = posts;

		imageLoader.init(ImageUtils.createConfiguration(context));
	}

	@Override
	public int getCount() {
		return posts.size();
	}

	@Override
	public PostEntry getItem(int position) {
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		PostEntry entry = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.posts_list_row, null);

			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.hubs = (TextView) view.findViewById(R.id.hubs);
			viewHolder.author = (TextView) view.findViewById(R.id.author);
			viewHolder.date = (TextView) view.findViewById(R.id.date);
			viewHolder.rating = (TextView) view.findViewById(R.id.rating);
			viewHolder.text = (TextView) view.findViewById(R.id.text);
			viewHolder.comments = (Button) view.findViewById(R.id.comments);
			viewHolder.voteUp = (ImageButton) view.findViewById(R.id.voteUp);
			viewHolder.voteDown = (ImageButton) view.findViewById(R.id.voteDown);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		viewHolder.title.setText(entry.getTitle());
		viewHolder.title.setTypeface(Fonts.ROBOTO_BOLD);

		if (entry.getAuthor() != null) {
			viewHolder.author.setText(entry.getAuthor());
			viewHolder.author.setTypeface(Fonts.ROBOTO_LIGHT);
		} else
			viewHolder.author.setVisibility(View.GONE);

		viewHolder.date.setText(entry.getDate());
		viewHolder.date.setTypeface(Fonts.ROBOTO_LIGHT);

		viewHolder.hubs.setText(entry.getHubs().get(0).getTitle());
		viewHolder.hubs.setTypeface(Fonts.ROBOTO_LIGHT);

		viewHolder.comments.setText(String.valueOf(entry.getCommentsCount()));
		viewHolder.comments.setTypeface(Fonts.ROBOTO_LIGHT);

		String text = entry.getText();
		if (text.length() > 200)
			text = text.substring(0, 199) + "...";
		viewHolder.text.setText(text);
		viewHolder.text.setTypeface(Fonts.ROBOTO_REGULAR);

		if (entry.getRating() != null)
			viewHolder.rating.setText(String.valueOf(entry.getRating()));
		else
			viewHolder.rating.setText("-");
		viewHolder.rating.setTypeface(Fonts.ROBOTO_LIGHT);

		if (!authApi.isAuth()) {
			viewHolder.voteUp.setEnabled(false);
			viewHolder.voteDown.setEnabled(false);
		}

		return view;
	}

	static class ViewHolder {
		TextView title;
		TextView hubs;
		TextView author;
		TextView date;
		TextView rating;
		TextView text;
		Button comments;
		ImageButton voteUp;
		ImageButton voteDown;
	}

}
