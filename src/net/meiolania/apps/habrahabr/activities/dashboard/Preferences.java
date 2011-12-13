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

package net.meiolania.apps.habrahabr.activities.dashboard;

import net.meiolania.apps.habrahabr.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class Preferences extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        signIn();
        rateApplication();
        showAttentionForFullscreen();
    }
    
    private void signIn(){
        Preference signIn = (Preference) findPreference("sign_in");
        signIn.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            
            public boolean onPreferenceClick(Preference preference){
                //startActivity(new Intent(Preferences.this, SignIn.class));
                Toast.makeText(Preferences.this, "В разработке", Toast.LENGTH_SHORT).show();
                return false;
            }
            
        });
    }
    
    private void showAttentionForFullscreen(){
        Preference fullScreen = (Preference) findPreference("fullscreen");
        fullScreen.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            
            public boolean onPreferenceClick(Preference preference){
                Toast.makeText(Preferences.this, R.string.preferences_fullscreen_summary_1, Toast.LENGTH_SHORT).show();
                return false;
            }
            
        });
    }

    private void rateApplication(){
        Preference rateApplication = (Preference) findPreference("rate_application");
        rateApplication.setOnPreferenceClickListener(new OnPreferenceClickListener(){

            public boolean onPreferenceClick(Preference preference){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Preferences.this.getPackageName()));
                startActivity(intent);
                return false;
            }

        });
    }

}