package net.meiolania.apps.habrahabr.slidemenu;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.MainActivity;
import net.meiolania.apps.habrahabr.auth.AuthFragment;
import net.meiolania.apps.habrahabr.auth.SignOutFragment;
import net.meiolania.apps.habrahabr.auth.User;
import net.meiolania.apps.habrahabr.fragments.companies.CompaniesFragment;
import net.meiolania.apps.habrahabr.fragments.events.EventsMainFragment;
import net.meiolania.apps.habrahabr.fragments.feed.FeedMainFragment;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsMainFragment;
import net.meiolania.apps.habrahabr.fragments.posts.PostsMainFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaMainFragment;
import net.meiolania.apps.habrahabr.fragments.users.UsersFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MenuFragment extends SherlockListFragment {
    private ArrayList<MenuData> menu;
    private MenuAdapter menuAdapter;

    private enum ItemType {
	AUTH, PROFILE, SIGN_OUT, FEED, FAVORITES, POSTS, HUBS, QA, EVENTS, COMPANIES, PEOPLE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	
	menu = new ArrayList<MenuData>();

	if (!User.getInstance().isLogged())
	    menu.add(new MenuData(R.string.auth, R.drawable.ic_menu_user, ItemType.AUTH));
	else {
	    menu.add(new MenuData(User.getInstance().getLogin(), R.drawable.ic_menu_user, ItemType.PROFILE));
	    menu.add(new MenuData(R.string.sign_out, R.drawable.ic_menu_user, ItemType.SIGN_OUT));
	    menu.add(new MenuData(R.string.feed, R.drawable.ic_menu_posts, ItemType.FEED));
	    menu.add(new MenuData(R.string.favorites, R.drawable.ic_menu_posts, ItemType.FAVORITES));
	}

	menu.add(new MenuData(R.string.posts, R.drawable.ic_menu_posts, ItemType.POSTS));
	menu.add(new MenuData(R.string.hubs, R.drawable.ic_menu_hubs, ItemType.HUBS));
	menu.add(new MenuData(R.string.qa, R.drawable.ic_menu_qa, ItemType.QA));
	menu.add(new MenuData(R.string.events, R.drawable.ic_menu_events, ItemType.EVENTS));
	menu.add(new MenuData(R.string.companies, R.drawable.ic_menu_companies, ItemType.COMPANIES));
	menu.add(new MenuData(R.string.people, R.drawable.ic_menu_user, ItemType.PEOPLE));

	menuAdapter = new MenuAdapter(getSherlockActivity(), menu);
	setListAdapter(menuAdapter);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
	MenuData data = menu.get(position);
	Fragment newContent = null;
	switch (data.itemType) {
	    case AUTH:
		newContent = new AuthFragment();
		break;
	    case SIGN_OUT:
		newContent = new SignOutFragment();
		break;
	    case FEED:
		newContent = new FeedMainFragment();
		break;
	    case POSTS:
		newContent = new PostsMainFragment();
		break;
	    case HUBS:
		newContent = new HubsMainFragment();
		break;
	    case QA:
		newContent = new QaMainFragment();
		break;
	    case EVENTS:
		newContent = new EventsMainFragment();
		break;
	    case COMPANIES:
		newContent = new CompaniesFragment();
		break;
	    case PEOPLE:
		newContent = new UsersFragment();
		break;
	}
	if (newContent != null)
	    switchFragment(newContent);
    }

    private void switchFragment(Fragment fragment) {
	if (getSherlockActivity() == null)
	    return;

	if (getSherlockActivity() instanceof MainActivity) {
	    MainActivity fca = (MainActivity) getSherlockActivity();
	    fca.switchContent(fragment);
	} else
	    startActivity(new Intent(getSherlockActivity(), MainActivity.class));
    }

    private class MenuData {
	public String title;
	public int icon;
	public ItemType itemType;

	public MenuData(int title, int icon, ItemType itemType) {
	    this(getString(title), icon, itemType);
	}

	public MenuData(String title, int icon, ItemType itemType) {
	    this.title = title;
	    this.icon = icon;
	    this.itemType = itemType;
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
	    title.setText(data.title);

	    return view;
	}

    }
}
