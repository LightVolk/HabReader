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

import com.markupartist.android.widget.ActionBar;

import net.meiolania.apps.habrahabr.R;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

public class SignIn extends ApplicationActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        
        setActionBar();
    }
    
    private void setActionBar(){
        ActionBar actionBar = (ActionBar)findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.preferences_sign_in);
    }
    
    private class DoSignIn extends AsyncTask<String, Void, Void>{
        private ProgressDialog progressDialog;
        private String login;
        private String password;
        
        @Override
        protected Void doInBackground(String... params){
            login = params[0];
            password = params[1];
            return null;
        }
        
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(SignIn.this);
            progressDialog.setMessage(getString(R.string.preferences_sign_in_1));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        
        @Override
        protected void onPostExecute(Void result){
            progressDialog.dismiss();
        }
        
    }
    
}