package com.columbusagain.camark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Rollno extends Activity {
	EditText rollno;
	Button submit;
	Activity mainactivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rollno);
		rollno = (EditText) findViewById(R.id.rollnotext);
		submit = (Button) findViewById(R.id.rollnobtn);
		rollno.setText("10P212");
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(rollno.getText().toString().compareTo("")==0)
				{
					Toast.makeText(mainactivity,"Please enter a roll number", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent(Rollno.this,MainActivity.class);
					intent.putExtra("rollno", rollno.getText().toString());
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
