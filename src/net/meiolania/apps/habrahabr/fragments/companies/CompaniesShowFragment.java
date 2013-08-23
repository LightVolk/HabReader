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
import android.content.Intent;
import android.net.Uri;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CompaniesShowFragment extends SherlockFragment implements
		LoaderCallbacks<CompanyFullData> {
	public final static String URL_ARGUMENT = "url";
	public final static int LOADER_COMPANY = 0;
	private String url;
	private ProgressDialog progressDialog;
	private String companyUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		url = getArguments().getString(URL_ARGUMENT);

		setRetainInstance(true);

		if (ConnectionUtils.isConnected(getSherlockActivity()))
			getSherlockActivity().getSupportLoaderManager().initLoader(
					LOADER_COMPANY, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.companies_show_activity, container,
				false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.companies_show_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.go_to_website:
				Uri uri = Uri.parse(companyUrl);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<CompanyFullData> onCreateLoader(int id, Bundle args) {
		showProgressDialog();

		CompaniesShowLoader loader = new CompaniesShowLoader(
				getSherlockActivity(), url);
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<CompanyFullData> loader,
			CompanyFullData data) {
		SherlockFragmentActivity activity = getSherlockActivity();

		if (activity != null && data != null) {
			ActionBar actionBar = activity.getSupportActionBar();
			actionBar.setTitle(data.getCompanyName());
			actionBar.setSubtitle(data.getDate());

			/* Company website */
			companyUrl = data.getCompanyUrl();

			/* Industries */
			TextView industriesDivider = (TextView) activity
					.findViewById(R.id.company_industries_divider);
			TextView industries = (TextView) activity
					.findViewById(R.id.company_industries);

			if (data.getIndustries() != null)
				industries.setText(data.getIndustries());
			else {
				industriesDivider.setVisibility(View.GONE);
				industries.setVisibility(View.GONE);
			}

			/* Location */
			TextView locationDivider = (TextView) activity
					.findViewById(R.id.company_location_divider);
			TextView location = (TextView) activity
					.findViewById(R.id.company_location);

			if (data.getLocation() != null)
				location.setText(data.getLocation());
			else {
				locationDivider.setVisibility(View.GONE);
				location.setVisibility(View.GONE);
			}

			/* Quantity */
			TextView quantityDivider = (TextView) activity
					.findViewById(R.id.company_quantity_divider);
			TextView quantity = (TextView) activity
					.findViewById(R.id.company_quantity);

			if (data.getQuantity() != null)
				quantity.setText(data.getQuantity());
			else {
				quantityDivider.setVisibility(View.GONE);
				quantity.setVisibility(View.GONE);
			}

			/* Summary */
			TextView summaryDivider = (TextView) activity
					.findViewById(R.id.company_summary_divider);
			WebView summary = (WebView) activity
					.findViewById(R.id.company_summary);

			if (data.getSummary() != null)
				summary.loadDataWithBaseURL("", data.getSummary(), "text/html",
						"UTF-8", null);
			else {
				summaryDivider.setVisibility(View.GONE);
				summary.setVisibility(View.GONE);
			}

			/* Management */
			TextView managementDivider = (TextView) activity
					.findViewById(R.id.company_management_divider);
			WebView management = (WebView) activity
					.findViewById(R.id.company_management);

			if (data.getManagement() != null)
				management.loadDataWithBaseURL("", data.getManagement(),
						"text/html", "UTF-8", null);
			else {
				managementDivider.setVisibility(View.GONE);
				management.setVisibility(View.GONE);
			}

			/* Development stages */
			TextView developmentStagesDivider = (TextView) activity
					.findViewById(R.id.company_development_stages_divider);
			WebView developmentStages = (WebView) activity
					.findViewById(R.id.company_development_stages);

			if (data.getDevelopmentStages() != null)
				developmentStages
						.loadDataWithBaseURL("", data.getDevelopmentStages(),
								"text/html", "UTF-8", null);
			else {
				developmentStagesDivider.setVisibility(View.GONE);
				developmentStages.setVisibility(View.GONE);
			}
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