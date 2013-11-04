package net.meiolania.apps.habrahabr.fragments.posts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.data.PostsFullData;
import net.meiolania.apps.habrahabr.utils.PostsSaverUtils;

import java.util.ArrayList;

public class PostSavedFragment extends SherlockListFragment
{
    PostsAdapter adapter;
    ArrayList<PostsFullData> posts;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        showActionBar();
        posts = PostsSaverUtils.getSavedPosts(getSherlockActivity());

        setRetainInstance(true);
        setHasOptionsMenu(false);

        adapter = new PostsAdapter(getActivity(),castDatas(posts));

        setListAdapter(adapter);
        registerForContextMenu(getListView());

        if (Preferences.getInstance(getSherlockActivity()).getAdditionalPosts()) {
            getListView().setDivider(null);
            getListView().setDividerHeight(0);
        }

        setEmptyText(getString(R.string.no_items_post));
    }
    private void showActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.removeAllTabs();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.saved);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    //TODO: hardcode !!!
    private static ArrayList<PostsData> castDatas(ArrayList<PostsFullData> datas)
    {
        ArrayList<PostsData> newDatas = new ArrayList<PostsData>();
        for(PostsFullData data : datas)
            newDatas.add(data);

        return newDatas;
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        showPost(position);
    }

    protected void showPost(int position) {

        Intent intent = new Intent(getSherlockActivity(), PostsShowActivity.class);
        intent.putExtra(PostsShowActivity.EXTRA_CONTENT,(Parcelable)posts.get(position));
        startActivity(intent);
    }


    private int choosenPosition;
    private final static int itemDeleteId = 352;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        choosenPosition = info.position;
        menu.add(0,itemDeleteId, 0, R.string.delete_post);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == itemDeleteId)
        {
            deletePost();
            return true;
        }
        return super.onContextItemSelected(item);
    }
    private ProgressDialog progressDialog;
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getSherlockActivity());
        progressDialog.setMessage(getString(R.string.deleting_post));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
    private void deletePost()
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
                PostsSaverUtils.deletePost(choosenPosition,getSherlockActivity());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();
            }
        }.execute();

        posts.remove(choosenPosition);
        adapter = new PostsAdapter(getActivity(),castDatas(posts));
        setListAdapter(adapter);
    }
}
