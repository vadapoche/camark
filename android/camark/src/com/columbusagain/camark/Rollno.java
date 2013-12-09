package com.columbusagain.camark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.columbusagain.camark.view.MyButton;

public class Rollno extends Activity {
	EditText rollno;
	MyButton submit;
	Activity mainactivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rollno);
		rollno = (EditText) findViewById(R.id.rollnotext);
		submit = (MyButton) findViewById(R.id.rollnobtn);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String rollnumber=rollno.getText().toString();
				// TODO Auto-generated method stub
				if ("".equals(rollnumber.trim())) {
					//Toast.makeText(mainactivity, "Please enter a Rollno",
					//		Toast.LENGTH_SHORT).show();
					
					Toast.makeText(getBaseContext(), "Please enter a Rollno", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(Rollno.this, MainActivity.class);
					
					intent.putExtra("rollno", rollnumber.trim());
					startActivity(intent);

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rollno, menu);
		return true;
	}

}
