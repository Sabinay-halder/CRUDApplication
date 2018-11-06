package com.silvermoon.crud_application.util;

import com.activeandroid.ActiveAndroid;


import android.app.Application;

public class MyApplication extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	ActiveAndroid.initialize(this);
	}
}
