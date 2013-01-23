package net.meiolania.apps.habrahabr.fragments.feed;

import net.meiolania.apps.habrahabr.fragments.qa.AbstractionQaFragment;

public class FeedQAFragment extends AbstractionQaFragment {
    public static final String URL = "http://habrahabr.ru/feed/qa/page%page%/";
    	
    @Override
    protected String getUrl() {
	return URL;
    }

    @Override
    protected int getLoaderId() {
	return 1;
    }

}