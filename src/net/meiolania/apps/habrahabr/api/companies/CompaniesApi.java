package net.meiolania.apps.habrahabr.api.companies;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.meiolania.apps.habrahabr.api.AuthApi;
import net.meiolania.apps.habrahabr.api.posts.PostEntry;
import net.meiolania.apps.habrahabr.api.utils.UrlUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class CompaniesApi {
	public final static String TAG = CompaniesApi.class.getSimpleName();
	private AuthApi authApi;

	public CompaniesApi(AuthApi authApi) {
		this.authApi = authApi;
	}

	public List<CompanyEntry> getCompanies(int page) {
		String url = UrlUtils.createUrl("companies/page", String.valueOf(page));
		return parseUrl(url);
	}

	private List<CompanyEntry> parseUrl(String url) {
		try {
			Log.i(TAG, "Loading a page: " + url);

			Document document = null;

			if (authApi.isAuth()) {
				Connection connection = Jsoup.connect(url);
				connection.cookie(AuthApi.AUTH_ID, authApi.getAuthId());
				connection.cookie(AuthApi.SESSION_ID, authApi.getSessionId());

				document = connection.get();
			} else {
				document = Jsoup.connect(url).get();
			}

			return parseDocument(document);
		} catch (IOException e) {
			Log.e(TAG, "Can't load a page: " + url + ". Error: " + e.getMessage());
		}

		return new ArrayList<CompanyEntry>();
	}

	private List<CompanyEntry> parseDocument(Document document) {
		List<CompanyEntry> companyEntries = new ArrayList<CompanyEntry>();

		Pattern subscriberPattern = Pattern.compile("[0-9]+");

		DecimalFormat decimalFormat = new DecimalFormat();

		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		formatSymbols.setDecimalSeparator(',');
		formatSymbols.setGroupingSeparator(' ');

		decimalFormat.setDecimalFormatSymbols(formatSymbols);

		Elements companies = document.select("div.companies_list > div.companies > div.company");
		for (Element company : companies) {
			Element name = company.select("div.description > div.name > a").first();
			Element icon = company.select("div.icon > img").first();
			Element index = company.select("div.habraindex").first();
			Element subscribers = company.select("div.description > p.fans_count").first();
			Element lastPost = company.select("div.description > p > a").first();

			CompanyEntry entry = new CompanyEntry();

			entry.setName(name.text());
			entry.setUrl(name.attr("abs:href"));

			if (!icon.attr("src").startsWith("http:"))
				entry.setLogo("http:" + icon.attr("src"));
			else
				entry.setLogo(icon.attr("src"));

			try {
				float indexValue = decimalFormat.parse(index.text()).floatValue();
				entry.setIndex(indexValue);
			} catch (ParseException e) {
			}

			Matcher subscriberMatcher = subscriberPattern.matcher(subscribers.text());
			if (subscriberMatcher.find())
				entry.setSubscribers(Integer.parseInt(subscriberMatcher.group()));

			if (lastPost != null) {
				PostEntry postEntry = new PostEntry();
				postEntry.setTitle(lastPost.text());
				postEntry.setUrl(lastPost.attr("abs:href"));

				entry.setLastPost(postEntry);
			}

			companyEntries.add(entry);
		}

		return companyEntries;
	}

}