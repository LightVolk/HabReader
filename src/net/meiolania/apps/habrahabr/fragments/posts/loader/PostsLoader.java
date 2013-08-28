package net.meiolania.apps.habrahabr.fragments.posts.loader;

import java.util.List;

import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.fragments.posts.PostsFragment;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PostsLoader extends AsyncTaskLoader<List<PostEntry>> {
	private PostsFragment fragment;
	private int page;
	
	public PostsLoader(Context context, PostsFragment fragment, int page) {
		super(context);
		
		this.fragment = fragment;
		this.page = page;
	}

	@Override
	public List<PostEntry> loadInBackground() {
		return fragment.getPosts(page);
	}
	
}