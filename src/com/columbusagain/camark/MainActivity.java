package com.columbusagain.camark;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	TextView view1;
	ProgressBar progress1;
	List<String> groupList;
	SparseArray<Groupmarks> testwisesparsearray;
	Map<String, Map<String, String>> subjectmarks;
	List<String> index;
	ExpandableListView expListView;
	ExpandableListAdapter explistadapter;
	Activity activity;
	Spinner spinner;
	SparseArray<Groupmarks> subjectwisesparsearray;
	List<SparseArray<Groupmarks>> subjectwisesparsearraylist = new ArrayList<SparseArray<Groupmarks>>();
	List<SparseArray<Groupmarks>> testwisesparsearraylist = new ArrayList<SparseArray<Groupmarks>>();

	List<ExpandableListView> expandableListView = new ArrayList<ExpandableListView>();

	ViewPager subjectwisePager, testwisePager;

	TextView mStudentName, mStudentRollNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		String rollno = intent.getExtras().getString("rollno").trim();
		progress1 = (ProgressBar) findViewById(R.id.progressBar1);
		expListView = (ExpandableListView) findViewById(R.id.mark_list);
		subjectwisePager = (ViewPager) findViewById(R.id.subjectWiseMarksPager);
		testwisePager = (ViewPager) findViewById(R.id.testWiseMarksPager);
		mStudentName = (TextView) findViewById(R.id.studentName);
		mStudentRollNo = (TextView) findViewById(R.id.studentRollNumber);
		Log.d("camark", "start");
		new FetchJson()
				.execute("http://citibytes.columbusagain.com/camark/striptable.php?rollno="
						+ rollno);
		Log.d("camark", "stop");
		activity = this;
		spinner = (Spinner) findViewById(R.id.spinner1);
		List<String> spinner_entries = new ArrayList<String>();
		spinner_entries.add("Subject wise");
		spinner_entries.add("Test wise");
		ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(
				activity, android.R.layout.simple_spinner_item, spinner_entries);
		spinneradapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinneradapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				String choice = parent.getItemAtPosition(pos).toString();
				if (choice == "Test wise") {
					displayTestwiseMarks();
				} else {
					if (subjectwisesparsearraylist.size() != 0) {
						displaySubjectwiseMarks();
					} else
						Log.d("DEBUG", "subjectwisesparsearraylist null");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int postion) {
			// TODO Auto-generated method stub
			return this.fragments.get(postion);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.fragments.size();
		}
	}

	private void displayTestwiseMarks() {
		subjectwisePager.setVisibility(View.GONE);
		testwisePager.setVisibility(View.VISIBLE);
		testwisePager.removeAllViews();
		List<Fragment> fragments = getFragments(testwisesparsearraylist.size(),
				testwisesparsearraylist);
		MyPageAdapter pageAdapter = new MyPageAdapter(
				getSupportFragmentManager(), fragments);
		pageAdapter.notifyDataSetChanged();
		testwisePager.setAdapter(pageAdapter);
	}

	private void displaySubjectwiseMarks() {
		testwisePager.setVisibility(View.GONE);
		subjectwisePager.removeAllViews();
		subjectwisePager.setVisibility(View.VISIBLE);
		List<Fragment> fragments = getFragments(
				subjectwisesparsearraylist.size(), subjectwisesparsearraylist);
		MyPageAdapter pageAdapter = new MyPageAdapter(
				getSupportFragmentManager(), fragments);
		pageAdapter.notifyDataSetChanged();
		subjectwisePager.setAdapter(pageAdapter);
	}

	private List<Fragment> getFragments(int count,
			List<SparseArray<Groupmarks>> sparseArrayList) {
		Log.d("count", count + " ");
		List<Fragment> fList = new ArrayList<Fragment>();
		for (int i = 0; i < count; i++) {
			fList.add(MyFragment.newInstance(sparseArrayList.get(i)));
		}
		return fList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class FetchJson extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			progress1.setVisibility(View.VISIBLE);
			URL url;
			HttpURLConnection urlconnection = null;
			InputStream in = null;
			String result = null;
			JSONArray json;
			index = new ArrayList<String>();
			groupList = new ArrayList<String>();

			try {
				url = new URL(params[0]);
				urlconnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlconnection.getInputStream());
				urlconnection.connect();
				Log.d("camark", "connection created");

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				in.close();
				result = sb.toString();
				reader.close();
				in.close();
				urlconnection.disconnect();

			} catch (Exception e) {
				return null;
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			progress1.setVisibility(View.INVISIBLE);
			if (result != null) {
				parseMarksJSON(result);
				displaySubjectwiseMarks();
			}
		}
	}

	private void parseMarksJSON(String result) {
		JSONObject marksJSON;
		try {
			marksJSON = new JSONObject(result);
			mStudentName.setText(marksJSON.getString("name"));
			mStudentRollNo.setText(marksJSON.getString("rollno"));
			JSONArray array = marksJSON.getJSONArray("data");
			Log.d("json", array.toString());
			result = array.getString(0);

			for (int k = 0; k < array.length(); k++) {
				Log.d("DEBUG", "k: " + array.length());
				JSONArray jsontable = array.getJSONArray(k);
				JSONArray jsonrow;
				Map<String, String> subjectmarktemp;
				if (!index.isEmpty())
					index.clear();
				if (!groupList.isEmpty())
					groupList.clear();
				subjectmarks = new HashMap<String, Map<String, String>>();
				for (int i = 0; i < jsontable.length(); i++) {
					jsonrow = jsontable.getJSONArray(i);
					for (int j = 0; j < jsonrow.length(); j++) {
						if (i == 0) {
							index.add(jsonrow.getString(j)); // collect the
																// index
																// alone
						} else if (i > 0 && j == 0) // collect the course
													// names
													// alone
						{
							groupList.add(jsonrow.getString(j));
						}
					}
				}
				for (int i = 1; i < jsontable.length(); i++) {
					jsonrow = jsontable.getJSONArray(i);
					subjectmarktemp = new HashMap<String, String>();
					for (int j = 1; j < jsonrow.length(); j++) {
						subjectmarktemp.put(index.get(j), jsonrow.getString(j));
					}
					subjectmarks.put(groupList.get(i - 1), subjectmarktemp);
				}

				Groupmarks testwisemarks;
				JSONArray indexjsonarray = new JSONArray();
				indexjsonarray = jsontable.getJSONArray(0);
				Marks marksobject = new Marks();
				testwisesparsearray = new SparseArray<Groupmarks>();
				for (int i = 1; i < indexjsonarray.length(); i++) {
					if (indexjsonarray.getString(i).compareTo("*") == 0)
						continue;
					testwisemarks = new Groupmarks(indexjsonarray.getString(i));
					Log.d("DEBUG", indexjsonarray.getString(i));
					marksobject = new Marks();
					Map<String, String> map = new HashMap<String, String>();
					for (int j = 1; j < jsontable.length(); j++) {
						JSONArray jsonarray = new JSONArray();
						jsonarray = jsontable.getJSONArray(j);
						marksobject.child.put(jsonarray.getString(0),
								jsonarray.getString(i));
					}

					testwisemarks.marks.add(marksobject);
					testwisesparsearray.append(i - 1, testwisemarks);
				}
				testwisesparsearraylist.add(testwisesparsearray);
				Log.d("DEBUG", "testwisesparsearraylist: "
						+ testwisesparsearraylist.size());

				subjectwisesparsearray = new SparseArray<Groupmarks>();
				Marks temp;
				Map<String, String> tempmap = new HashMap<String, String>();
				Groupmarks groupMarks = null;
				int i = 0;
				for (Entry entry : subjectmarks.entrySet()) {

					groupMarks = new Groupmarks((String) entry.getKey());
					temp = new Marks();
					tempmap = subjectmarks.get(entry.getKey());
					for (Entry subentry : tempmap.entrySet()) {
						String key = subentry.getKey().toString();
						String value = subentry.getValue().toString();
						if (key.compareTo("*") == 0) // sometimes json array
														// may contain * as
														// an entry like
														// CA-1,CA-2.
														// To avoid that,
														// this checking is
														// needed
							continue;
						temp.child.put(key, value);
					}
					groupMarks.marks.add(temp);
					subjectwisesparsearray.append(i, groupMarks);
					i++;
				}
				subjectwisesparsearraylist.add(subjectwisesparsearray);
				Log.d("DEBUG", "subjectwisesparsearraylist: "
						+ subjectwisesparsearraylist.size());
			}// End of jsontable forloop
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}