package com.vadapoche.camark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vadapoche.camark.R;
import com.vadapoche.camark.view.MyTextView;

public class Rollno extends Activity {

	private boolean mInternetAvailable;

	private EditText mRollNumber;

	private Button mSubmit;

	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rollno);
		mRollNumber = (EditText) findViewById(R.id.rollnotext);
		mSubmit = (Button) findViewById(R.id.rollnobtn);
		mActivity = this;
		mSubmit.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				if (!mInternetAvailable) {
					Toast.makeText(Rollno.this,
							"Please check your internet...", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (mRollNumber.getText().toString().trim().length()==0) {
					Toast.makeText(mActivity, "Please enter a roll number",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(Rollno.this, MainActivity.class);
					intent.putExtra("rollno", mRollNumber.getText().toString());
					startActivity(intent);

				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		mInternetAvailable = NetworkChecker.isConnected(Rollno.this);
		MyTextView errorMsg = (MyTextView) findViewById(R.id.noInternetMessage);
		if (mInternetAvailable)
			errorMsg.setVisibility(View.INVISIBLE);
		else
			errorMsg.setVisibility(View.VISIBLE);
	}

	public void showDevelopers(View v) {
		Intent intent = new Intent(Rollno.this, Developers.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rollno, menu);
		return true;
	}

}
