package net.meiolania.apps.habrahabr.ui;

import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionProvider;

public class PageActionProvider extends ActionProvider {
    private Context context;
    private TextView pageView;
    private int page;
    
    public PageActionProvider(Context context) {
	super(context);
	this.context = context;
    }

    @Override
    public View onCreateActionView() {
	LayoutInflater layoutInflater = LayoutInflater.from(context);
	View view = layoutInflater.inflate(R.layout.page_action_provider, null);
	
	pageView = (TextView) view.findViewById(R.id.page);
	pageView.setText(String.valueOf(page));
	
	return view;
    }
    
    public void setPage(int page) {
	this.page = page;
    }

}