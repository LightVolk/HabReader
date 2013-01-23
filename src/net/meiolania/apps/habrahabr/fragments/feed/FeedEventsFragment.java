package net.meiolania.apps.habrahabr.fragments.feed;

import net.meiolania.apps.habrahabr.fragments.events.AbstractionEventsFragment;

public class FeedEventsFragment extends AbstractionEventsFragment {
    public static final String URL = "http://habrahabr.ru/feed/events/coming/page%page%/";
    
    @Override
    protected String getUrl() {
	return URL;
    }

    @Override
    protected int getLoaderId() {
	return 2;
    }

}