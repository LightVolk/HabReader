package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.SideMenuFragment.MenuData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SideMenuAdapter extends BaseAdapter {
	private ArrayList<MenuData> data;
	private Context context;
	private LayoutInflater layoutInflater;

	public SideMenuAdapter(Context context, ArrayList<MenuData> data) {
		this.data = data;
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		if (!data.isSection) {

			View view = layoutInflater.inflate(R.layout.slide_menu_row, null);

			TextView title = (TextView) view.findViewById(R.id.slide_menu_title);

			Drawable img = context.getResources().getDrawable(data.icon);
			title.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

			title.setText(data.title);

			return view;
		} else {
			View view = layoutInflater.inflate(R.layout.slide_menu_section_row, null);

			TextView title = (TextView) view.findViewById(R.id.slide_menu_title);

			title.setText(data.title);

			return view;
		}
	}

}