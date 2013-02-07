package net.meiolania.apps.habrahabr.fragments.posts;

import com.actionbarsherlock.app.SherlockDialogFragment;

import net.meiolania.apps.habrahabr.R;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommentDialogFragment extends SherlockDialogFragment {
    String comment;

    public CommentDialogFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	comment = getArguments().getString(PostsCommentsFragment.EXTRA_COMMENT_BODY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_comment, container);

	TextView commentBody = (TextView) getSherlockActivity().findViewById(R.id.comment_body);
	commentBody.setText(comment);
	return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	
	Log.w("COMMENT", comment);
    }
}