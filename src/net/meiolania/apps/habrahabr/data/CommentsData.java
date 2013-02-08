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

public class CommentsData {
    public final static int MAX_LEVEL = 5;
    protected String url;
    protected String author;
    protected String authorUrl;
    protected String comment;
    protected String score;
    protected String time;
    protected int level;

    public String getAuthorUrl() {
	return authorUrl;
    }

    public void setAuthorUrl(String url) {
	this.authorUrl = url;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getScore() {
	return score;
    }

    public void setScore(String score) {
	this.score = score;
    }
    
    public String getTime() {
	return time;
    }
    
    public void setTime(String time) {
	this.time = time;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }
}