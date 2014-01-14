package com.columbusagain.camark;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Developers extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_developers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.developers, menu);
		return true;
	}

}
