package net.meiolania.apps.habrahabr.fragments.conversations;

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.ConversationAdapter;
import net.meiolania.apps.habrahabr.api.conversations.ConversationEntry;
import net.meiolania.apps.habrahabr.fragments.conversations.loader.ConversationsLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;

public class ConversationsFragment extends SherlockListFragment implements LoaderCallbacks<List<ConversationEntry>>, OnScrollListener {
	public final static int LOADER_CONVERSATIONS = 0;
	private List<ConversationEntry> conversations;
	private ConversationAdapter adapter;
	private boolean isLoadData;
	private int page = 1;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setRetainInstance(true);
		setHasOptionsMenu(true);

		showActionBar();

		if (adapter == null) {
			conversations = new ArrayList<ConversationEntry>();
			adapter = new ConversationAdapter(getSherlockActivity(), conversations);
		}

		setListAdapter(adapter);
		setListShown(false);

		getListView().setOnScrollListener(this);
	}

	private void showActionBar() {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.removeAllTabs();
		actionBar.setTitle(R.string.conversations);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	private void restartLoading() {
		if (ConnectionUtils.isConnected(getSherlockActivity())) {
			LoaderManager loaderManager = getSherlockActivity().getSupportLoaderManager();
			loaderManager.restartLoader(LOADER_CONVERSATIONS, null, this);

			isLoadData = true;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
			restartLoading();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public Loader<List<ConversationEntry>> onCreateLoader(int id, Bundle args) {
		ConversationsLoader loader = new ConversationsLoader(getSherlockActivity(), page);
		loader.forceLoad();
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<ConversationEntry>> loader, List<ConversationEntry> data) {
		conversations.addAll(data);

		adapter.notifyDataSetChanged();

		isLoadData = false;
		page++;
		
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<List<ConversationEntry>> loader) {

	}

}