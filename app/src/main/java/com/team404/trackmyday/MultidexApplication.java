package com.team404.trackmyday;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class MultidexApplication extends Application {

	private static Context context;

	// private Twitter twitter;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		


	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static void setExternalContext(Context oContext) {
		context = oContext;
	}

	public static Context getAppContext() {
		return context;
	}

}