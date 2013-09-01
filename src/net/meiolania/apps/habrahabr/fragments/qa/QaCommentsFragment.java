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

package net.meiolania.apps.habrahabr.fragments.qa;

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.api.comments.CommentEntry;
import net.meiolania.apps.habrahabr.fragments.qa.loader.QaCommentsLoader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.SherlockListFragment;

public class QaCommentsFragment extends SherlockListFragment implements LoaderCallbacks<List<CommentEntry>> {
	public final static int LOADER_COMMENTS = 1;
	public final static int MENU_OPEN_AUTHOR_PROFILE = 0;
	public final static String URL_ARGUMENT = "url";
	private List<CommentEntry> comments;
	private CommentsAdapter adapter;
	private String url;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		url = getArguments().getString(URL_ARGUMENT);

		setRetainInstance(true);

		if (adapter == null) {
			comments = new ArrayList<CommentEntry>();
			adapter = new CommentsAdapter(getSherlockActivity(), comments);
		}

		setListAdapter(adapter);
		setListShown(false);

		registerForContextMenu(getListView());

		getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_COMMENTS, null, this);

		setEmptyText(getString(R.string.no_items_comments));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		menu.add(0, MENU_OPEN_AUTHOR_PROFILE, 0, R.string.open_author_profile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		CommentEntry commentsData = (CommentEntry) getListAdapter().getItem(adapterContextMenuInfo.position);

		switch (item.getItemId()) {
			case MENU_OPEN_AUTHOR_PROFILE:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(commentsData.getAuthorUrl())));
				break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public Loader<List<CommentEntry>> onCreateLoader(int id, Bundle args) {
		QaCommentsLoader loader = new QaCommentsLoader(getSherlockActivity(), url);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<CommentEntry>> loader, List<CommentEntry> data) {
		if (comments.isEmpty()) {
			comments.addAll(data);
			adapter.notifyDataSetChanged();
		}

		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<List<CommentEntry>> loader) {

	}

}