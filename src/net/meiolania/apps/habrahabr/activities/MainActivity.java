package net.meiolania.apps.habrahabr.activities;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import net.meiolania.apps.habrahabr.slidemenu.MenuFragment;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {

    private Fragment mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

	setContentView(R.layout.menu_frame);
	setBehindContentView(R.layout.menu_frame);

	SlidingMenu slidingMenu = getSlidingMenu();
	slidingMenu.setMode(SlidingMenu.LEFT);
	slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	slidingMenu.setShadowDrawable(R.drawable.sm_shadow);
	slidingMenu.setShadowWidth(50);
	slidingMenu.setFadeDegree(0.2f);
	slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	slidingMenu.setMenu(R.layout.slide_menu);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	setSupportProgressBarIndeterminateVisibility(false);
	// set the Above View
	if (savedInstanceState != null)
	    mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
	if (mContent == null)
	    mContent = new PostsMainFragment();

	// set the Above View
	setContentView(R.layout.content_frame);
	getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();

	// set the Behind View
	setBehindContentView(R.layout.menu_frame);
	getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

	if (!ConnectionUtils.isConnected(this)) {
	    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setTitle(R.string.error);
	    dialog.setMessage(getString(R.string.no_connection));
	    dialog.setPositiveButton(R.string.close, getConnectionDialogListener());
	    dialog.setCancelable(false);
	    dialog.show();
	}

	User.getInstance().init(this);
    }

    // @Override
    // public void onSaveInstanceState(Bundle outState) {
    // super.onSaveInstanceState(outState);
    // getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    // }

    public void switchContent(Fragment fragment) {
	mContent = fragment;
	getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

	// It's not good
	// TODO: Think about that
	Handler h = new Handler();
	h.postDelayed(new Runnable() {
	    public void run() {
		getSlidingMenu().showContent();
	    }
	}, 50);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater menuInflater = getSupportMenuInflater();
	menuInflater.inflate(R.menu.global_activity, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    toggle();
	    return true;
	case R.id.preferences:
	    startActivity(new Intent(this, PreferencesActivity.class));
	    return true;
	case R.id.more_applications:
	    Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Andrey+Zaytsev");
	    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    startActivity(intent);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
	if (getSlidingMenu().isMenuShowing())
	    toggle();
	else
	    super.onBackPressed();
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
