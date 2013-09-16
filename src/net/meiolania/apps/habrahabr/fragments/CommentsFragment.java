package net.meiolania.apps.habrahabr.fragments;

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.api.comments.CommentEntry;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class CommentsFragment extends SherlockFragment implements LoaderCallbacks<List<CommentEntry>> {
	public final static int LOADER_COMMENTS = 1;
	public final static int MENU_OPEN_AUTHOR_PROFILE = 0;
	public final static String URL_ARGUMENT = "url";

	protected String url;

	private List<CommentEntry> comments;
	private CommentsAdapter adapter;

	private ListView commentsList;
	private EditText commentText;
	private ImageButton sendButton;

	protected abstract View getFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		url = getArguments().getString(URL_ARGUMENT);

		setRetainInstance(true);

		if (adapter == null) {
			comments = new ArrayList<CommentEntry>();
			adapter = new CommentsAdapter(getSherlockActivity(), comments);
		}

		commentsList.setAdapter(adapter);
		registerForContextMenu(commentsList);

		LoaderManager loaderManager = getSherlockActivity().getSupportLoaderManager();
		loaderManager.initLoader(LOADER_COMMENTS, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getFragmentView(inflater, container, savedInstanceState);

		commentsList = (ListView) view.findViewById(R.id.comments_list);
		commentText = (EditText) view.findViewById(R.id.comment);
		sendButton = (ImageButton) view.findViewById(R.id.send_comment);

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		menu.add(0, MENU_OPEN_AUTHOR_PROFILE, 0, R.string.open_author_profile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		CommentEntry data = adapter.getItem(adapterContextMenuInfo.position);

		switch (item.getItemId()) {
			case MENU_OPEN_AUTHOR_PROFILE:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getAuthorUrl())));
				break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onLoadFinished(Loader<List<CommentEntry>> loader, List<CommentEntry> data) {
		if (comments.isEmpty() && data != null) {
			comments.addAll(data);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<CommentEntry>> loader) {

	}

}