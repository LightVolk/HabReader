package net.meiolania.apps.habrahabr.fragments.conversations;

import java.util.ArrayList;
import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.ConversationAdapter;
import net.meiolania.apps.habrahabr.api.conversations.ConversationEntry;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;

public class ConversationsFragment extends SherlockListFragment implements LoaderCallbacks<List<ConversationEntry>> {
	public final static int LOADER_CONVERSATIONS = 0;
	private List<ConversationEntry> conversations;
	private ConversationAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setRetainInstance(true);
		setHasOptionsMenu(true);
		
		showActionBar();
		
		if (adapter == null) {
			conversations = new ArrayList<ConversationEntry>();
			adapter = new ConversationAdapter();
		}
		
		setListAdapter(adapter);
		setListShown(false);
		
		if (ConnectionUtils.isConnected(getSherlockActivity())) {
			LoaderManager loaderManager = getSherlockActivity().getSupportLoaderManager();
			loaderManager.restartLoader(LOADER_CONVERSATIONS, null, this);
		}
	}
	
	private void showActionBar() {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.removeAllTabs();
		actionBar.setTitle(R.string.conversations);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public Loader<List<ConversationEntry>> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<List<ConversationEntry>> loader, List<ConversationEntry> data) {
		
	}

	@Override
	public void onLoaderReset(Loader<List<ConversationEntry>> loader) {
		
	}
	
}