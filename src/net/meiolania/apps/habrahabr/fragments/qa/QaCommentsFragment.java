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

package net.meiolania.apps.habrahabr.fragments.qa;

import java.util.List;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.comments.CommentEntry;
import net.meiolania.apps.habrahabr.fragments.CommentsFragment;
import net.meiolania.apps.habrahabr.fragments.qa.loader.QaCommentsLoader;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QaCommentsFragment extends CommentsFragment {

	@Override
	public Loader<List<CommentEntry>> onCreateLoader(int id, Bundle args) {
		QaCommentsLoader commentsLoader = new QaCommentsLoader(getSherlockActivity(), url);
		commentsLoader.forceLoad();

		return commentsLoader;
	}

	@Override
	protected View getFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_qa_comments, container, false);
	}

}