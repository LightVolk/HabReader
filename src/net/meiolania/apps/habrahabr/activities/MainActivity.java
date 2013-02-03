package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.AbstractionFragmentActivity;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends AbstractionFragmentActivity {
    public final static String DEVELOPER_PLAY_LINK = "https://play.google.com/store/apps/developer?id=Andrey+Zaytsev";
    private Fragment content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	if (content == null)
	    content = new PostsMainFragment();

	setContentView(R.layout.content_frame);
	getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();
    }

    public void switchContent(Fragment fragment) {
	content = fragment;
	getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

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