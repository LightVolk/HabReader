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

import java.io.Serializable;

public class PostsData implements Serializable {
    protected String title;
    protected String url;
    protected String hubs;
    protected String author;
    protected String date;
    protected String comments;
    protected String score;
    protected String image;
    protected String text;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getHubs() {
	return hubs;
    }

    public void setHubs(String hubs) {
	this.hubs = hubs;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getDate() {
	return date;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public void setScore(String score) {
	this.score = score;
    }

    public String getScore() {
	return score;
    }
    
    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    /** Text before {@code</habracut>} */
    public void setText(String text) {
        this.text = text;
    }

    /** @return text before {@code</habracut>} */
    public String getText() {
        return text;
    }
}