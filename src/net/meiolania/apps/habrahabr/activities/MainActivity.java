package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends AbstractionFragmentActivity {
    private Fragment content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	if (content == null)
	    content = new PostsMainFragment();

	getSupportFragmentManager().beginTransaction().replace(android.R.id.content, content).commit();
    }

    public void switchContent(Fragment fragment) {
	content = fragment;
	getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

	toggle();
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	switch(item.getItemId()) {
	    case android.R.id.home:
		toggle();
		return true;
	}
        return super.onMenuItemSelected(featureId, item);
    }

    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	};
    }

}