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

package net.meiolania.apps.habrahabr.fragments.companies;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CompanyFullData;
import net.meiolania.apps.habrahabr.fragments.companies.loader.CompaniesShowLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class CompaniesShowFragment extends SherlockFragment implements LoaderCallbacks<CompanyFullData> {
    public final static String URL_ARGUMENT = "url";
    public final static int LOADER_COMPANY = 0;
    private String url;
    private ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	url = getArguments().getString(URL_ARGUMENT);

	setRetainInstance(true);

	if (ConnectionUtils.isConnected(getSherlockActivity()))
	    getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_COMPANY, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.companies_show_activity, container, false);
    }

    @Override
    public Loader<CompanyFullData> onCreateLoader(int id, Bundle args) {
	showProgressDialog();

	CompaniesShowLoader loader = new CompaniesShowLoader(getSherlockActivity(), url);
	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<CompanyFullData> loader, CompanyFullData data) {
	SherlockFragmentActivity activity = getSherlockActivity();

	if (activity != null) {
	    ActionBar actionBar = activity.getSupportActionBar();
	    actionBar.setTitle(data.getCompanyName());
	    
	    TextView date = (TextView) activity.findViewById(R.id.company_date);
	    date.setText(data.getDate());

	    TextView site = (TextView) activity.findViewById(R.id.company_site);
	    site.setText(data.getCompanyUrl());

	    TextView industries = (TextView) activity.findViewById(R.id.company_industries);
	    industries.setText(data.getIndustries());

	    TextView location = (TextView) activity.findViewById(R.id.company_location);
	    location.setText(data.getLocation());

	    TextView quantity = (TextView) activity.findViewById(R.id.company_quantity);
	    quantity.setText(data.getQuantity());

	    WebView summary = (WebView) activity.findViewById(R.id.company_summary);
	    summary.getSettings().setSupportZoom(true);
	    summary.loadDataWithBaseURL("", data.getSummary(), "text/html", "UTF-8", null);

	    WebView management = (WebView) activity.findViewById(R.id.company_management);
	    management.getSettings().setSupportZoom(true);
	    management.loadDataWithBaseURL("", data.getManagement(), "text/html", "UTF-8", null);

	    WebView developmentStages = (WebView) activity.findViewById(R.id.company_development_stages);
	    developmentStages.getSettings().setSupportZoom(true);
	    developmentStages.loadDataWithBaseURL("", data.getDevelopmentStages(), "text/html", "UTF-8", null);
	}

	hideProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<CompanyFullData> loader) {

    }

    private void showProgressDialog() {
	progressDialog = new ProgressDialog(getSherlockActivity());
	progressDialog.setMessage(getString(R.string.loading_company));
	progressDialog.setCancelable(true);
	progressDialog.show();
    }

    private void hideProgressDialog() {
	if (progressDialog != null)
	    progressDialog.dismiss();
    }

}