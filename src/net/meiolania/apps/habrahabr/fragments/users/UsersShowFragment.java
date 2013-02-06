/*
Copyright 2012-2013 Andrey Zaytsev, Sergey Ivanov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package net.meiolania.apps.habrahabr.fragments.users;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.UsersFullData;
import net.meiolania.apps.habrahabr.fragments.users.loader.UsersShowLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class UsersShowFragment extends SherlockFragment implements LoaderCallbacks<UsersFullData> {
    public final static String URL_ARGUMENT = "url";
    public final static int LOADER_PEOPLE = 0;
    private String url;
    private ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	url = getArguments().getString(URL_ARGUMENT);

	if (ConnectionUtils.isConnected(getSherlockActivity()))
	    getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_PEOPLE, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.users_show_activity, container, false);
    }

    @Override
    public Loader<UsersFullData> onCreateLoader(int id, Bundle args) {
	showProgressDialog();

	UsersShowLoader loader = new UsersShowLoader(getSherlockActivity(), url);
	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<UsersFullData> loader, UsersFullData data) {
	SherlockFragmentActivity activity = getSherlockActivity();

	if (activity != null) {
	    ActionBar actionBar = activity.getSupportActionBar();
	    actionBar.setTitle(data.getUsername());

	    /* Show avatar */
	    ImageView avatar = (ImageView) activity.findViewById(R.id.avatar);
	    ImageLoader imageLoader = ImageLoader.getInstance();
	    imageLoader.init(ImageLoaderConfiguration.createDefault(getSherlockActivity()));
	    imageLoader.displayImage(data.getAvatar(), avatar);

	    if (data.getFullname().length() > 0)
		actionBar.setSubtitle(data.getFullname());

	    /* Karma */
	    TextView karma = (TextView) activity.findViewById(R.id.karma);
	    if (data.getKarma().length() > 0)
		karma.setText(getString(R.string.karma_count_s, data.getKarma()));
	    else
		karma.setVisibility(View.GONE);

	    /* Rating */
	    TextView rating = (TextView) activity.findViewById(R.id.rating);
	    if (data.getRating().length() > 0)
		rating.setText(getString(R.string.rating_count_s, data.getRating()));
	    else
		rating.setVisibility(View.GONE);

	    /* Birthday */
	    TextView birthday = (TextView) activity.findViewById(R.id.birthday);
	    if (data.getBirthday().length() > 0)
		birthday.setText(data.getBirthday());
	    else
		birthday.setVisibility(View.GONE);

	    /* Interests */
	    TextView interestsDivider = (TextView) activity.findViewById(R.id.interests_divider);
	    TextView interests = (TextView) activity.findViewById(R.id.interests);

	    if (data.getInterests().length() <= 0) {
		interestsDivider.setVisibility(View.GONE);
		interests.setVisibility(View.GONE);
	    } else
		interests.setText(data.getInterests());

	    /* Summary */
	    TextView summaryDivider = (TextView) activity.findViewById(R.id.summary_divider);
	    TextView summary = (TextView) activity.findViewById(R.id.summary);

	    if (data.getSummary().length() <= 0) {
		summaryDivider.setVisibility(View.GONE);
		summary.setVisibility(View.GONE);
	    } else
		summary.setText(Html.fromHtml(data.getSummary()));
	}

	hideProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<UsersFullData> loader) {

    }

    private void showProgressDialog() {
	progressDialog = new ProgressDialog(getSherlockActivity());
	progressDialog.setMessage(getString(R.string.loading_profile_info));
	progressDialog.setCancelable(true);
	progressDialog.show();
    }

    private void hideProgressDialog() {
	if (progressDialog != null)
	    progressDialog.dismiss();
    }

}