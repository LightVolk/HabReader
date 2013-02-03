package net.meiolania.apps.habrahabr.auth;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;

import com.actionbarsherlock.app.SherlockFragment;

public class SignOutFragment extends SherlockFragment {
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	Preferences preferences = Preferences.getInstance(getSherlockActivity());
	preferences.setPHPSessionId(null);
	preferences.setLogin(null);
	preferences.setHSecId(null);
	
	CookieManager.getInstance().removeAllCookie();
	
	User.getInstance().init(getSherlockActivity());

	Intent intent = new Intent(getSherlockActivity(), MainActivity.class);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	startActivity(intent);
    }
    
}