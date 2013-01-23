package net.meiolania.apps.habrahabr.fragments.feed;

import net.meiolania.apps.habrahabr.fragments.posts.AbstractionPostsFragment;

public class FeedPostsFragment extends AbstractionPostsFragment {
    public static final String URL = "http://habrahabr.ru/feed/posts/page%page%/";
    
    @Override
    protected String getUrl() {
	return URL;
    }

    @Override
    protected int getLoaderId() {
	return 0;
    }

}