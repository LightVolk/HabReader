/*
Copyright 2012 Andrey Zaytsev

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
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PostsAdapter extends BaseAdapter {
    private ArrayList<PostsData> posts;
    private Context context;
    private boolean additionalLayout = false;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public PostsAdapter(Context context, ArrayList<PostsData> posts) {
        this.context = context;
        this.posts = posts;

        Preferences preferences = Preferences.getInstance(context);
        this.additionalLayout = preferences.getAdditionalPosts();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheSize(3000000)
                .maxImageWidthForMemoryCache(200).discCacheSize(50000000).httpReadTimeout(5000)
                .defaultDisplayImageOptions(options).build();
        this.imageLoader.init(configuration);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public PostsData getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostsData data = getItem(position);

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.posts_list_row, null);
        }

        ImageView image = (ImageView)view.findViewById(R.id.post_image);
        if (!TextUtils.isEmpty(data.getImage())) {
            image.setVisibility(View.VISIBLE);
            imageLoader.displayImage(data.getImage(), image);
        } else {
            image.setVisibility(View.GONE);
        }

        TextView text = (TextView)view.findViewById(R.id.post_text);
        text.setText(data.getText());

        TextView title = (TextView)view.findViewById(R.id.post_title);
        title.setText(data.getTitle());

        TextView hubs = (TextView)view.findViewById(R.id.post_hubs);
        TextView author = (TextView)view.findViewById(R.id.post_author);
        TextView date = (TextView)view.findViewById(R.id.post_date);
        TextView score = (TextView)view.findViewById(R.id.post_score);

        RelativeLayout postInfo = (RelativeLayout)view.findViewById(R.id.post_info);

        if (additionalLayout) {
            hubs.setText(data.getHubs());
            author.setText(data.getAuthor());
            date.setText(data.getDate());

            Integer rating = UIUtils.parseRating(data.getScore());
            if (rating != null) {
                score.setVisibility(View.VISIBLE);

                if (rating > 0) {
                    score.setTextColor(context.getResources().getColor(R.color.rating_positive));
                } else if (rating < 0) {
                    score.setTextColor(context.getResources().getColor(R.color.rating_negative));
                } else {
                    score.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
            score.setText(data.getScore());
        } else {
            hubs.setVisibility(View.GONE);
            postInfo.setVisibility(View.GONE);
        }

        return view;
    }

}
