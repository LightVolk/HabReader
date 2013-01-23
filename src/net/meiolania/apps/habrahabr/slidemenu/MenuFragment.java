package net.meiolania.apps.habrahabr.slidemenu;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import net.meiolania.apps.habrahabr.fragments.companies.CompaniesFragment;
import net.meiolania.apps.habrahabr.fragments.events.EventsMainFragment;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsFragment;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsMainFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaMainFragment;
import net.meiolania.apps.habrahabr.fragments.users.UsersFragment;

import com.actionbarsherlock.app.SherlockListFragment;

import net.meiolania.apps.habrahabr.activities.AuthActivity;
import net.meiolania.apps.habrahabr.activities.FeedActivity;
import net.meiolania.apps.habrahabr.activities.HubsActivity;
import net.meiolania.apps.habrahabr.activities.SignOutActivity;
import net.meiolania.apps.habrahabr.auth.User;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends SherlockListFragment {
    private ArrayList<MenuData> menu;
    private MenuAdapter menuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	menu = new ArrayList<MenuData>();

	// Auth
	//if (!User.getInstance().isLogged())
	//    menu.add(new MenuData(getString(R.string.auth), R.drawable.ic_menu_user, AuthActivity.class));
	//else {
	//    menu.add(new MenuData(User.getInstance().getLogin(), R.drawable.ic_menu_user, null));
	//    menu.add(new MenuData(getString(R.string.sign_out), R.drawable.ic_menu_user, SignOutActivity.class));
	//    menu.add(new MenuData(getString(R.string.feed), R.drawable.ic_menu_posts, FeedActivity.class));
	//}

	menu.add(new MenuData(R.string.posts, R.drawable.ic_menu_posts));
	menu.add(new MenuData(R.string.hubs, R.drawable.ic_menu_hubs));
	menu.add(new MenuData(R.string.qa, R.drawable.ic_menu_qa));
	menu.add(new MenuData(R.string.events, R.drawable.ic_menu_events));
	menu.add(new MenuData(R.string.companies, R.drawable.ic_menu_companies));
	menu.add(new MenuData(R.string.people, R.drawable.ic_menu_user));

	menuAdapter = new MenuAdapter(getSherlockActivity(), menu);
	setListAdapter(menuAdapter);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
	Fragment newContent = null;
	switch (position) {
	case 0:
	    newContent = new PostsMainFragment();
	    break;
	case 1:
	    newContent = new HubsFragment();
	    break;
	case 2:
	    newContent = new QaMainFragment();
	    break;
	case 3:
	    newContent = new EventsMainFragment();
	    break;
	case 4:
	    newContent = new CompaniesFragment();
	    break;
	case 5:
	    newContent = new UsersFragment();
	    break;
	}
	if (newContent != null)
	    switchFragment(newContent);
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
	if (getSherlockActivity() == null)
	    return;

	MainActivity fca = (MainActivity) getSherlockActivity();
	fca.switchContent(fragment);
    }

    private class MenuData {
	public int title;
	public int icon;

	public MenuData(int title, int icon) {
	    this.title = title;
	    this.icon = icon;
	}
    }

    private class MenuAdapter extends BaseAdapter {
	private ArrayList<MenuData> data;
	private Context context;

	public MenuAdapter(Context context, ArrayList<MenuData> data) {
	    this.data = data;
	    this.context = context;
	}

	@Override
	public int getCount() {
	    return data.size();
	}

	@Override
	public MenuData getItem(int position) {
	    return data.get(position);
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    MenuData data = getItem(position);

	    View view = convertView;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.slide_menu_row, null);
	    }

	    TextView title = (TextView) view.findViewById(R.id.slide_menu_title);

	    Drawable img = context.getResources().getDrawable(data.icon);
	    title.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
	    title.setText(context.getResources().getString(data.title));

	    return view;
	}

    }
}
