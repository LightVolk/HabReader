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
import net.meiolania.apps.habrahabr.api.comments.CommentEntry;
import net.meiolania.apps.habrahabr.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentsAdapter extends BaseAdapter {
	private List<CommentEntry> comments;
	private Context context;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private LayoutInflater layoutInflater;
	private int commentLevelMargin;
	private int commentLeftMargin;
	private int commentTopMargin;
	private int commentRightMargin;
	private int commentBottomMargin;

	public CommentsAdapter(Context context, List<CommentEntry> comments) {
		this.comments = comments;
		this.context = context;

		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		commentLevelMargin = context.getResources().getDimensionPixelOffset(R.dimen.comment_level_margin);
		commentLeftMargin = context.getResources().getDimensionPixelOffset(R.dimen.comment_left_margin);
		commentTopMargin = context.getResources().getDimensionPixelOffset(R.dimen.comment_top_margin);
		commentRightMargin = context.getResources().getDimensionPixelOffset(R.dimen.comment_right_margin);
		commentBottomMargin = context.getResources().getDimensionPixelOffset(R.dimen.comment_bottom_margin);
		
		imageLoader.init(ImageUtils.createConfiguration(context));
	}

	public int getCount() {
		return comments.size();
	}

	public CommentEntry getItem(int position) {
		return comments.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		CommentEntry data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.comments_list_row, null);

			viewHolder = new ViewHolder();
			
			viewHolder.container = (RelativeLayout) view.findViewById(R.id.container);
			viewHolder.infoContainer = (RelativeLayout) view.findViewById(R.id.info_container);
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar);
			viewHolder.author = (TextView) view.findViewById(R.id.author);
			viewHolder.date = (TextView) view.findViewById(R.id.date);
			viewHolder.rating = (TextView) view.findViewById(R.id.rating);
			viewHolder.text = (TextView) view.findViewById(R.id.text);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		int leftPadding = commentLeftMargin + data.getLevel() * commentLevelMargin;
		viewHolder.container.setPadding(leftPadding,
										viewHolder.container.getPaddingTop(),
										viewHolder.container.getPaddingRight(),
										viewHolder.container.getPaddingBottom());

		viewHolder.author.setText(data.getAuthor());
		viewHolder.author.setTypeface(Fonts.ROBOTO_BOLD);
		
		viewHolder.date.setText(data.getDate());
		viewHolder.date.setTypeface(Fonts.ROBOTO_LIGHT);

		if (data.getRating() != null)
			viewHolder.rating.setText(String.valueOf(data.getRating()));
		else
			viewHolder.rating.setText("-");
		viewHolder.rating.setTypeface(Fonts.ROBOTO_LIGHT);

		viewHolder.text.setText(data.getText());
		viewHolder.text.setTypeface(Fonts.ROBOTO_REGULAR);

		imageLoader.displayImage(data.getAvatarUrl(), viewHolder.avatar);

		return view;
	}

	static class ViewHolder {
		RelativeLayout container;
		RelativeLayout infoContainer;
		ImageView avatar;
		TextView author;
		TextView date;
		TextView rating;
		TextView text;
	}

}