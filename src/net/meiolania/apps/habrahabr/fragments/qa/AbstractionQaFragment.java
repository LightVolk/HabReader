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

package net.meiolania.apps.habrahabr.fragments.qa;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.QaSearchActivity;
import net.meiolania.apps.habrahabr.activities.QaShowActivity;
import net.meiolania.apps.habrahabr.adapters.QaAdapter;
import net.meiolania.apps.habrahabr.data.QaData;
import net.meiolania.apps.habrahabr.fragments.qa.loader.QaLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class AbstractionQaFragment extends SherlockListFragment implements OnScrollListener, LoaderCallbacks<ArrayList<QaData>>{
	private int page;
	private boolean isLoadData;

	protected ArrayList<QaData> qaDatas;
	protected QaAdapter qaAdapter;

	protected abstract String getUrl();

	protected abstract int getLoaderId();

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		setRetainInstance(true);
		setHasOptionsMenu(true);

		if(qaAdapter == null){
			qaDatas = new ArrayList<QaData>();
			qaAdapter = new QaAdapter(getSherlockActivity(), qaDatas);
		}

		setListAdapter(qaAdapter);
		setListShown(true);
		
		getListView().setDivider(null);
		getListView().setDividerHeight(0);

		getListView().setOnScrollListener(this);

		if(getSherlockActivity().getSupportLoaderManager().getLoader(getLoaderId()) == null)
			getSherlockActivity().getSupportLoaderManager().initLoader(getLoaderId(), null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.qa_fragment, menu);

		final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
		searchQuery.setOnEditorActionListener(new OnEditorActionListener(){
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					Intent intent = new Intent(getSherlockActivity(), QaSearchActivity.class);
					intent.putExtra(QaSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
					startActivity(intent);
					return true;
				}
				return false;
			}
		});

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id){
		showQa(position);
	}

	protected void showQa(int position){
		QaData qaData = qaDatas.get(position);

		Intent intent = new Intent(getSherlockActivity(), QaShowActivity.class);
		intent.putExtra(QaShowActivity.EXTRA_URL, qaData.getUrl());
		intent.putExtra(QaShowActivity.EXTRA_TITLE, qaData.getTitle());

		startActivity(intent);
	}

	protected void restartLoading(){
		if(!isLoadData){
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

			QaLoader.setPage(++page);

			getSherlockActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, this);

			isLoadData = true;
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
		if((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
			restartLoading();
	}

	public void onScrollStateChanged(AbsListView view, int scrollState){

	}

	@Override
	public Loader<ArrayList<QaData>> onCreateLoader(int id, Bundle args){
		QaLoader loader = new QaLoader(getSherlockActivity(), getUrl());
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<QaData>> loader, ArrayList<QaData> data){
		qaDatas.addAll(data);
		qaAdapter.notifyDataSetChanged();

		if(getSherlockActivity() != null)
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

		isLoadData = false;
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<QaData>> loader){

	}

}