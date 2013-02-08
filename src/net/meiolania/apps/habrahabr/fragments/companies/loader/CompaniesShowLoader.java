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

package net.meiolania.apps.habrahabr.fragments.companies.loader;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.CompanyFullData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class CompaniesShowLoader extends AsyncTaskLoader<CompanyFullData> {
    public final static String TAG = CompaniesShowLoader.class.getName();

    public final static int INFO_DATE = 0;
    public final static int INFO_SITE = 1;
    public final static int INFO_INDUSTRIES = 2;
    public final static int INFO_LOCATION = 3;
    public final static int INFO_QUANTITY = 4;
    public final static int INFO_SUMMARY = 5;
    public final static int INFO_MANAGEMENT = 6;
    public final static int INFO_DEVELOPMENT_STAGES = 7;

    private String url;

    public CompaniesShowLoader(Context context, String url) {
	super(context);

	this.url = url;
    }

    @Override
    public CompanyFullData loadInBackground() {
	CompanyFullData company = new CompanyFullData();

	try {
	    Log.i(TAG, "Loading a page: " + url);

	    Document document = Jsoup.connect(url).get();

	    Element state = document.select("div.state").first();
	    if (state != null)
		company.setDeletedInfo(state.text());
	    else {

		Elements datas = document.select("div.company_profile > dl");
		Element companyname = document.select("head > title").first();
		company.setTitle(companyname.text().replace(" / Хабрахабр", ""));

		int i = 0;
		Element temp;
		for (Element data : datas) {
		    switch (i) {
			case INFO_DATE:
			    temp = data.getElementsByTag("dd").first();
			    company.setDate(temp != null ? temp.text() : "");
			    break;
			case INFO_SITE:
			    temp = data.getElementsByTag("dd").first();
			    company.setCompanyUrl(temp != null ? temp.text() : "");
			    break;
			case INFO_INDUSTRIES:
			    temp = data.getElementsByTag("dd").first();
			    company.setIndustries(temp != null ? temp.text() : "");
			    break;
			case INFO_LOCATION:
			    temp = data.getElementsByTag("dd").first();
			    company.setLocation(temp != null ? temp.text() : "");
			    break;
			case INFO_QUANTITY:
			    temp = data.getElementsByTag("dd").first();
			    company.setQuantity(temp != null ? temp.text() : "");
			    break;
			case INFO_SUMMARY:
			    temp = data.getElementsByTag("dd").first();
			    company.setSummary(temp != null ? temp.text() : "");
			    break;
			case INFO_MANAGEMENT:
			    Elements managers = data.getElementsByTag("dd");
			    StringBuilder managerContent = new StringBuilder();

			    for (Element manager : managers)
				managerContent.append(manager.html());
			    company.setManagement(managerContent.toString());
			    break;
			case INFO_DEVELOPMENT_STAGES:
			    Elements stages = data.getElementsByTag("dd");
			    StringBuilder stagesContent = new StringBuilder();

			    for (Element stage : stages)
				stagesContent.append(stage.html());
			    company.setDevelopmentStages(stagesContent.toString());
			    break;
		    }
		    i++;
		}
	    }
	} catch (IOException e) {
	}

	return company;
    }

}