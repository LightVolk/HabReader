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

package net.meiolania.apps.habrahabr.auth;

import net.meiolania.apps.habrahabr.Preferences;
import android.content.Context;

public class User {
    public static final String PHPSESSION_ID = "PHPSESSID";
    public static final String HSEC_ID = "hsec_id";
    private static User instance = null;
    private String login;
    private String phpsessid;
    private String hsecid;
    private boolean isLogged = false;

    public static User getInstance() {
	if (instance == null)
	    instance = new User();
	return instance;
    }

    public void init(Context context) {
	Preferences preferences = Preferences.getInstance(context);
	login = preferences.getLogin();
	phpsessid = preferences.getPHPSessionId();
	hsecid = preferences.getHSecId();

	// TODO: think more about this?
	if (login != null && phpsessid != null && hsecid != null)
	    isLogged = true;
	else
	    isLogged = false;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getPhpsessid() {
	return phpsessid;
    }

    public void setPhpsessid(String phpsessid) {
	this.phpsessid = phpsessid;
    }

    public String getHsecid() {
	return hsecid;
    }

    public void setHsecid(String hsecid) {
	this.hsecid = hsecid;
    }

    public boolean isLogged() {
	return isLogged;
    }

}