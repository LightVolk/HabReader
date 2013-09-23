package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Fonts;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.SideMenuFragment.SideMenuData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SideMenuAdapter extends BaseAdapter {
	private final static int ITEM_LAYOUT = 0;
	private final static int SECTION_LAYOUT = 1;
	private ArrayList<SideMenuData> items;
	private LayoutInflater layoutInflater;

	public SideMenuAdapter(Context context, ArrayList<SideMenuData> items) {
		this.items = items;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public SideMenuData getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return (items.get(position).isSection ? SECTION_LAYOUT : ITEM_LAYOUT);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		SideMenuData data = getItem(position);

		ViewHolder viewHolder;
		if (view == null) {
			if (getItemViewType(position) == ITEM_LAYOUT)
				view = layoutInflater.inflate(R.layout.side_menu_row, null);
			else
				view = layoutInflater.inflate(R.layout.side_menu_section_row, null);

			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
			viewHolder.divider = view.findViewById(R.id.divider);

			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		viewHolder.title.setText(data.title);

		if (!data.isSection)
			viewHolder.title.setTypeface(Fonts.ROBOTO_REGULAR);
		else
			viewHolder.title.setTypeface(Fonts.ROBOTO_BOLD);
		
		if (viewHolder.icon != null)
			viewHolder.icon.setImageResource(data.icon);

		if ((position + 1) < (items.size() - 1) && items.get(position + 1).isSection)
			viewHolder.divider.setVisibility(View.INVISIBLE);
		else
			viewHolder.divider.setVisibility(View.VISIBLE);

		return view;
	}

	static class ViewHolder {
		TextView title;
		ImageView icon;
		View divider;
	}

}