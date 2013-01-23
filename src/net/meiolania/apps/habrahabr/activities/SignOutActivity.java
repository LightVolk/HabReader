package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.auth.User;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;

public class SignOutActivity extends AbstractionFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	Preferences preferences = Preferences.getInstance(this);
	preferences.setPHPSessionId(null);
	preferences.setLogin(null);
	preferences.setHSecId(null);
	
	CookieManager.getInstance().removeAllCookie();
	
	User.getInstance().init(this);

	Intent intent = new Intent(this, MainActivity.class);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	startActivity(intent);
    }

    @Override
    protected OnClickListener getConnectionDialogListener() {
	return null;
    }

}