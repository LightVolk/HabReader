/*
Copyright 2012-2013 Andrey Zaytsev, Sergey Ivanov

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

import net.meiolania.apps.habrahabr.R;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingController.IConfiguration;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction.PurchaseState;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class PreferencesActivity extends SherlockPreferenceActivity {
    private final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs7Q63tPakTKbvVItiO4w9XzwASiV/bjTd7L6yYOvJc/iN3uJDt2LgZ4c7O2r+WzBNdg1TC9hW3GfPIGX5BC8mxXUCJ4Oq05XFP4UGsuj8lP5/GaV0S2a9vlYFMa4e6RiFekanGjej4REsoGQUZIA5mwCO+zWtvQjFe8acj8zD06GfUBmPuSbnDHuXVIvfpVRCjLOXT4qJTlnaNptFKKrNO0A6nzRIhNWxsV2QRMYCY6ZgqOsRHIOc92kBZhltm+XTwMTi+BgxBRUhvhMt2rtHJz7p6dh3xkSDFMOK6yQNAYWlNllhgj2cl2EkeYZOiK/wZQud1N1cirneRgJW2A7ZwIDAQAB";
    private final static String ANDROID_DONATE_ITEM = "donate.1dollar";
    private AbstractBillingObserver billingObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	showActionBar();

	addPreferencesFromResource(R.xml.preferences);

	donate();
    }

    @Override
    protected void onDestroy() {
	BillingController.unregisterObserver(billingObserver);
	super.onDestroy();
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(R.string.preferences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    finish();
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    private void donate() {
	final Preference donate = (Preference) findPreference("donate");

	billingObserver = new AbstractBillingObserver(this) {

	    public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
	    }

	    public void onPurchaseStateChanged(String itemId, PurchaseState state) {
	    }

	    public void onBillingChecked(boolean supported) {
		if (!supported)
		    donate.setEnabled(false);
		else if (!billingObserver.isTransactionsRestored())
		    BillingController.restoreTransactions(PreferencesActivity.this);
	    }

	    @Override
	    public void onSubscriptionChecked(boolean supported) {

	    }
	};

	// BillingController.setDebug(true);
	BillingController.setConfiguration(new IConfiguration() {

	    public String getPublicKey() {
		return PUBLIC_KEY;
	    }

	    public byte[] getObfuscationSalt() {
		return new byte[] { 41, -90, -116, -41, 66, -53, 122, -110, -127, -96, -88, 77, 127, 115, 1, 73, 57, 110, 48, -116 };
	    }

	});
	BillingController.registerObserver(billingObserver);
	BillingController.checkBillingSupported(this);

	donate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    public boolean onPreferenceClick(Preference preference) {
		BillingController.requestPurchase(PreferencesActivity.this, ANDROID_DONATE_ITEM, true, null);
		return false;
	    }
	});

	boolean purchased = BillingController.isPurchased(this, ANDROID_DONATE_ITEM);
	if (purchased) {
	    donate.setTitle(R.string.donate_thanks_preferences);
	    donate.setEnabled(false);
	}
    }

}