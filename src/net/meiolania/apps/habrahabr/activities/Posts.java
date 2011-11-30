/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
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

package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.utils.Vibrate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class Posts extends ApplicationActivity{
    private final ArrayList<PostsData> postsDataList = new ArrayList<PostsData>();
    private PostsAdapter postsAdapter;
    private int page;
    private Document document;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        setActionBar();
        loadList();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_SEARCH:
                startActivity(new Intent(this, PostsSearch.class));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Preferences.vibrate)
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.favorites:
                startActivity(new Intent(this, FavoritesPosts.class));
                break;
        }
        return true;
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.posts);
        actionBar.addAction(new LoadNextPageAction());
        actionBar.addAction(new UpdateAction());
        actionBar.addAction(new IntentAction(this, new Intent(this, PostsSearch.class), R.drawable.actionbar_ic_search));
    }

    private class LoadNextPageAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_forward;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private class UpdateAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_update;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private void loadList(){
        ++page;
        new LoadPostsList().execute();
    }

    private class LoadPostsList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                document = Jsoup.connect("http://habrahabr.ru/blogs/page" + page + "/").get();

                Elements postsList = document.select("div.post");

                for(Element post : postsList){
                    PostsData postsData = new PostsData();

                    Element title = post.select("a.post_title").first();
                    Element blog = post.select("a.blog_title").first();

                    postsData.setTitle(title.text());
                    postsData.setBlog(blog.text());
                    postsData.setLink(title.attr("abs:href"));

                    postsDataList.add(postsData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Posts.this);
            progressDialog.setMessage(getString(R.string.loading_posts_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                postsAdapter = new PostsAdapter(Posts.this, postsDataList);

                ListView listView = (ListView) Posts.this.findViewById(R.id.posts_list);
                listView.setAdapter(postsAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                        PostsData postsData = postsDataList.get(position);

                        Intent intent = new Intent(Posts.this, PostsShow.class);
                        intent.putExtra("link", postsData.getLink());

                        Posts.this.startActivity(intent);
                    }
                });
            }else
                postsAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }

    }

}