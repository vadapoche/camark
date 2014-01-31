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
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.columbusagain.camark.view.MyTextView;

public class MainActivity extends FragmentActivity {

	private boolean mInternetAvailable;

	private ProgressBar mLoadingProgress;

	private List<String> mGroupList;

	private SparseArray<Groupmarks> mTestwiseSparseArray;

	private Map<String, Map<String, String>> mSubjectMarks;

	private List<String> mIndex;

	private Activity mActivity;
	
	private MyTextView NameView;
	
	private MyTextView RollnoView;

	private String name;
	
	private String rollno;
	
	private Spinner mSpinner;

	private SparseArray<Groupmarks> mSubjectwiseSparseArray;

	private List<SparseArray<Groupmarks>> mSubjectwiseSparseArrayList = new ArrayList<SparseArray<Groupmarks>>();

	private List<SparseArray<Groupmarks>> mTestwiseSparseArrayList = new ArrayList<SparseArray<Groupmarks>>();

	private ViewPager subjectwisePager, testwisePager;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		String rollno = intent.getExtras().getString("rollno").trim();
		mLoadingProgress = (ProgressBar) findViewById(R.id.progressBar1);
		subjectwisePager = (ViewPager) findViewById(R.id.subjectWiseMarksPager);
		testwisePager = (ViewPager) findViewById(R.id.testWiseMarksPager);
		NameView	=	 (MyTextView) findViewById(R.id.studentName);
		RollnoView  = (MyTextView) findViewById(R.id.studentRollNumber);
		
		Log.d("camark", "start");
		new FetchJson()
				.execute("http://camark.vadapoche.in/striptable.php?rollno="
						+ rollno);
		mActivity = this;
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		List<String> spinner_entries = new ArrayList<String>();
		spinner_entries.add("Subject wise");
		spinner_entries.add("Test wise");
		ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(
				mActivity, android.R.layout.simple_spinner_item,
				spinner_entries);
		spinneradapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(spinneradapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String choice = parent.getItemAtPosition(pos).toString();
				if (choice == "Test wise") {
					displayTestwiseMarks();
				} else {
					if (mSubjectwiseSparseArrayList.size() != 0) {
						displaySubjectwiseMarks();
					} else
						Log.d("camark", "subjectwisesparsearraylist null");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		mInternetAvailable = NetworkChecker.isConnected(MainActivity.this);
		MyTextView errorMsg = (MyTextView) findViewById(R.id.noInternetMessage);
		if (mInternetAvailable)
			errorMsg.setVisibility(View.INVISIBLE);
		else
			errorMsg.setVisibility(View.VISIBLE);
	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int postion) {
			return this.fragments.get(postion);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	private void displayTestwiseMarks() {
		subjectwisePager.setVisibility(View.GONE);
		testwisePager.setVisibility(View.VISIBLE);
		testwisePager.removeAllViews();
		List<Fragment> fragments = getFragments(
				mTestwiseSparseArrayList.size(), mTestwiseSparseArrayList);
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
				mSubjectwiseSparseArrayList.size(), mSubjectwiseSparseArrayList);
		MyPageAdapter pageAdapter = new MyPageAdapter(
				getSupportFragmentManager(), fragments);
		pageAdapter.notifyDataSetChanged();
		subjectwisePager.setAdapter(pageAdapter);
	}

	private List<Fragment> getFragments(int count,
			List<SparseArray<Groupmarks>> sparseArrayList) {
		Log.d("camark", count + " ");
		List<Fragment> fList = new ArrayList<Fragment>();
		for (int i = 0; i < count; i++) {
			fList.add(MyFragment.newInstance(sparseArrayList.get(i)));
		}
		return fList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class FetchJson extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			mLoadingProgress.setVisibility(View.VISIBLE);
			URL url;
			HttpURLConnection urlconnection = null;
			InputStream in = null;
			String result = null;
			mIndex = new ArrayList<String>();
			mGroupList = new ArrayList<String>();

			try {
				url = new URL(params[0]);
				urlconnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlconnection.getInputStream());
				urlconnection.connect();
				Log.d("camark", "connection created");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
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
				JSONObject object = new JSONObject(result);
				JSONArray array = object.getJSONArray("data");
				name			= object.getString("name");
				rollno			=	object.getString("rollno");
				result = array.getString(0);
					
				for (int k = 0; k < array.length(); k++) {
					Log.d("camark", "k: " + array.length());
					JSONArray jsontable = array.getJSONArray(k);
					JSONArray jsonrow;
					Map<String, String> subjectmarktemp;
					if (!mIndex.isEmpty())
						mIndex.clear();
					if (!mGroupList.isEmpty())
						mGroupList.clear();
					mSubjectMarks = new HashMap<String, Map<String, String>>();
					for (int i = 0; i < jsontable.length(); i++) {
						jsonrow = jsontable.getJSONArray(i);
						for (int j = 0; j < jsonrow.length(); j++) {
							if (i == 0) {
								mIndex.add(jsonrow.getString(j)); // collect the
																	// index
																	// alone
							} else if (i > 0 && j == 0) // collect the course
														// names
														// alone
							{
								mGroupList.add(jsonrow.getString(j));
							}
						}
					}
					for (int i = 1; i < jsontable.length(); i++) {
						jsonrow = jsontable.getJSONArray(i);
						subjectmarktemp = new HashMap<String, String>();
						for (int j = 1; j < jsonrow.length(); j++) {
							subjectmarktemp.put(mIndex.get(j),
									jsonrow.getString(j));
						}
						mSubjectMarks.put(mGroupList.get(i - 1),
								subjectmarktemp);
					}

					Groupmarks testwisemarks;
					JSONArray indexjsonarray = new JSONArray();
					indexjsonarray = jsontable.getJSONArray(0);
					Marks marksobject = new Marks();
					mTestwiseSparseArray = new SparseArray<Groupmarks>();
					for (int i = 1; i < indexjsonarray.length(); i++) {
						if (indexjsonarray.getString(i).compareTo("*") == 0)
							continue;
						testwisemarks = new Groupmarks(
								indexjsonarray.getString(i));
						Log.d("camark", indexjsonarray.getString(i));
						marksobject = new Marks();
						Map<String, String> map = new HashMap<String, String>();
						for (int j = 1; j < jsontable.length(); j++) {
							JSONArray jsonarray = new JSONArray();
							jsonarray = jsontable.getJSONArray(j);
							marksobject.child.put(jsonarray.getString(0),
									jsonarray.getString(i));
						}

						testwisemarks.marks.add(marksobject);
						mTestwiseSparseArray.append(i - 1, testwisemarks);
					}
					mTestwiseSparseArrayList.add(mTestwiseSparseArray);
					Log.d("camark", "testwisesparsearraylist: "
							+ mTestwiseSparseArrayList.size());

					mSubjectwiseSparseArray = new SparseArray<Groupmarks>();
					Marks temp;
					Map<String, String> tempmap = new HashMap<String, String>();
					Groupmarks groupMarks = null;
					int i = 0;
					for (Entry entry : mSubjectMarks.entrySet()) {

						groupMarks = new Groupmarks((String) entry.getKey());
						temp = new Marks();
						tempmap = mSubjectMarks.get(entry.getKey());
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
						mSubjectwiseSparseArray.append(i, groupMarks);
						i++;
					}
					mSubjectwiseSparseArrayList.add(mSubjectwiseSparseArray);
					Log.d("camark", "subjectwisesparsearraylist: "
							+ mSubjectwiseSparseArrayList.size());
				}// End of jsontable forloop

			} catch (Exception e) {
				return null;
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			displaySubjectwiseMarks();
			mLoadingProgress.setVisibility(View.INVISIBLE);
			Log.d("DEBUG","rollno");
			RollnoView.setText(rollno);
			NameView.setText(name);
		}
	}

}