package net.meiolania.apps.habrahabr.adapters;

import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.conversations.ConversationEntry;
import net.meiolania.apps.habrahabr.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ConversationAdapter extends BaseAdapter {
	private List<ConversationEntry> conversations;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private LayoutInflater layoutInflater;
	
	public ConversationAdapter(Context context, List<ConversationEntry> conversations) {
		this.conversations = conversations;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader.init(ImageUtils.createConfiguration(context));
	}
	
	@Override
	public int getCount() {
		return conversations.size();
	}

	@Override
	public ConversationEntry getItem(int position) {
		return conversations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ConversationEntry entry = getItem(position);
		
		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.conversation_list_row, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.date = (TextView) view.findViewById(R.id.date);
			viewHolder.message = (TextView) view.findViewById(R.id.message);
			
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		viewHolder.name.setText(entry.getAuthorName());
		viewHolder.date.setText(entry.getTime());
		viewHolder.message.setText(entry.getMessage());
		
		imageLoader.displayImage(entry.getAuthorAvatar(), viewHolder.avatar);
		
		return view;
	}
	
	static class ViewHolder {
		ImageView avatar;
		TextView name;
		TextView date;
		TextView message;
	}

}