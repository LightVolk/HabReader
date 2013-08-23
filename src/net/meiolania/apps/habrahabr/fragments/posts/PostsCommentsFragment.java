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

package net.meiolania.apps.habrahabr.fragments.posts;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostCommentsLoader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.SherlockListFragment;

public class PostsCommentsFragment extends SherlockListFragment implements
		LoaderCallbacks<ArrayList<CommentsData>> {
	public final static int LOADER_COMMENTS = 1;
	public final static int MENU_OPEN_COMMENT_IN_BROWSER = 0;
	public final static int MENU_OPEN_AUTHOR_PROFILE = 1;
	public final static String URL_ARGUMENT = "url";
	public final static String EXTRA_COMMENT_AUTHOR = "author";
	public final static String EXTRA_COMMENT_BODY = "comment";
	public final static String EXTRA_COMMENT_SCORE = "score";
	public final static String EXTRA_COMMENT_TIME = "time";
	private ArrayList<CommentsData> comments;
	private CommentsAdapter adapter;
	private String url;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		url = getArguments().getString(URL_ARGUMENT);

		setRetainInstance(true);

		if (adapter == null) {
			comments = new ArrayList<CommentsData>();
			adapter = new CommentsAdapter(getSherlockActivity(), comments);
		}

		setListAdapter(adapter);
		setListShown(false);

		getListView().setDivider(null);
		getListView().setDividerHeight(0);

		registerForContextMenu(getListView());

		getSherlockActivity().getSupportLoaderManager().initLoader(
				LOADER_COMMENTS, null, this);

		setEmptyText(getString(R.string.no_items_comments));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		menu.add(0, MENU_OPEN_COMMENT_IN_BROWSER, 0,
				R.string.open_comment_in_browser);
		menu.add(0, MENU_OPEN_AUTHOR_PROFILE, 0, R.string.open_author_profile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		CommentsData commentsData = (CommentsData) getListAdapter().getItem(
				adapterContextMenuInfo.position);

		switch (item.getItemId()) {
			case MENU_OPEN_COMMENT_IN_BROWSER:
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(commentsData.getUrl())));
				break;
			case MENU_OPEN_AUTHOR_PROFILE:
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(commentsData.getAuthorUrl())));
				break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		CommentsData data = (CommentsData) getListAdapter().getItem(position);

		CommentDialogFragment dFragment = new CommentDialogFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		Bundle arguments = new Bundle();
		arguments.putString(EXTRA_COMMENT_AUTHOR, data.getAuthor());
		arguments.putString(EXTRA_COMMENT_BODY, data.getComment());
		arguments.putString(EXTRA_COMMENT_SCORE, data.getScore());
		arguments.putString(EXTRA_COMMENT_TIME, data.getTime());

		dFragment.setArguments(arguments);
		dFragment.show(ft, "dialog");
	}

	@Override
	public Loader<ArrayList<CommentsData>> onCreateLoader(int id, Bundle args) {
		PostCommentsLoader loader = new PostCommentsLoader(
				getSherlockActivity(), url);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<CommentsData>> loader,
			ArrayList<CommentsData> data) {
		if (comments.isEmpty()) {
			comments.addAll(data);
			adapter.notifyDataSetChanged();
		}

		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<CommentsData>> loader) {

	}

}