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

package net.meiolania.apps.habrahabr.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PostsFullData extends PostsData implements Serializable,Parcelable{
    protected String content;

    //for Serializable
    private static final long serialVersionUID = 1L;

    public void setContent(String content) {
	this.content = content;
    }

    public String getContent() {
	return content;
    }

    public PostsFullData()
    {}

    //for Parcelable

    @Override
    public int describeContents() {
        return 0;
    }
    public PostsFullData(Parcel source)
    {
        title = source.readString();
        url = source.readString();
        hubs = source.readString();
        author = source.readString();
        date = source.readString();
        comments = source.readString();
        score = source.readString();
        image = source.readString();
        text = source.readString();
        content =  source.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(hubs);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(comments);
        dest.writeString(score);
        dest.writeString(image);
        dest.writeString(text);
        dest.writeString(content);
    }
    public static final Parcelable.Creator<PostsFullData> CREATOR = new Parcelable.Creator<PostsFullData>()
    {
        @Override
        public PostsFullData createFromParcel(Parcel source)
        {
            return new PostsFullData(source);
        }

        @Override
        public PostsFullData[] newArray(int size)
        {
            return new PostsFullData[size];
        }
    };

}