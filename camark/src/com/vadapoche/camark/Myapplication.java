package com.vadapoche.camark;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;

import android.app.Application;
import android.util.Log;

@ReportsCrashes
(
		formKey = "",
		httpMethod = Method.PUT,
	    reportType = Type.JSON,
	    formUri = "http://camark.vadapoche.in:5984/acra-camark/_design/acra-storage/_update/report",
	    formUriBasicAuthLogin = "camark",
	    formUriBasicAuthPassword = "camark"
)

public class Myapplication extends Application {


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("DEBUG","Inside Application class");
		ACRA.init(this);
	}
	
}
