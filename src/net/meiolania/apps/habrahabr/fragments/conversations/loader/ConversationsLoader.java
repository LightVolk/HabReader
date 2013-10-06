package net.meiolania.apps.habrahabr.fragments.conversations.loader;

import java.util.List;

import net.meiolania.apps.habrahabr.HabrAuthApi;
import net.meiolania.apps.habrahabr.api.conversations.ConversationEntry;
import net.meiolania.apps.habrahabr.api.conversations.ConversationsApi;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ConversationsLoader extends AsyncTaskLoader<List<ConversationEntry>> {
	private int page;

	public ConversationsLoader(Context context, int page) {
		super(context);
		this.page = page;
	}

	@Override
	public List<ConversationEntry> loadInBackground() {
		ConversationsApi conversationsApi = new ConversationsApi(HabrAuthApi.getInstance());
		return conversationsApi.getConversations(page);
	}

}