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

import com.columbusagain.camark.view.MyTextView;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ProgressBar mLoadingProgressBar;

	private Activity mActivity;

	private Spinner mSpinner;

	private List<String> mGroupList;

	private List<String> mIndex;

	private int mCurrentPage;

	private int mTotalPages;

	private LinearLayout mDotsScrollBarHolder;

	private SparseArray<Groupmarks> mTestwiseSparseArray;

	private SparseArray<Groupmarks> mSubjectwiseSparseArray;

	private Map<String, Map<String, String>> mSubjectToMarks;

	private List<SparseArray<Groupmarks>> mSubjectwiseSparseArrayList = new ArrayList<SparseArray<Groupmarks>>();

	private List<SparseArray<Groupmarks>> mTestwiseSpareArrayList = new ArrayList<SparseArray<Groupmarks>>();

	private ViewPager mSubjectwiseViewPager, mTestwiseViewPager;

	private MyTextView mStudentName, mStudentRollNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		itemsFindViewById();
		initializeViewPagerListener();
		Intent intent = getIntent();
		String rollno = intent.getExtras().getString("rollno").trim();
		Log.d("camark", "start");
		new FetchJson()
				.execute("http://54.254.199.87/camark/striptable.php?rollno="
						+ rollno);
		Log.d("camark", "stop");
		mActivity = this;
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
					mTotalPages = mTestwiseSpareArrayList.size();
					mCurrentPage = mTestwiseViewPager.getCurrentItem();
					updateIndicator(mCurrentPage);
				} else {
					if (mSubjectwiseSparseArrayList.size() != 0) {
						displaySubjectwiseMarks();
						mTotalPages = mSubjectwiseSparseArrayList.size();
						mCurrentPage = mSubjectwiseViewPager.getCurrentItem();
						updateIndicator(mCurrentPage);
					} else
						Log.d("DEBUG", "subjectwisesparsearraylist null");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void itemsFindViewById() {
		mLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mSubjectwiseViewPager = (ViewPager) findViewById(R.id.subjectWiseMarksPager);
		mTestwiseViewPager = (ViewPager) findViewById(R.id.testWiseMarksPager);
		mStudentName = (MyTextView) findViewById(R.id.studentName);
		mStudentRollNo = (MyTextView) findViewById(R.id.studentRollNumber);
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		mDotsScrollBarHolder = (LinearLayout) findViewById(R.id.dotsScrollbarHolder);
	}

	private void initializeViewPagerListener() {
		mSubjectwiseViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						mCurrentPage = position;
						updateIndicator(mCurrentPage);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

		mTestwiseViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						mCurrentPage = position;
						updateIndicator(mCurrentPage);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						switch (state) {
						case 0:
							updateIndicator(mCurrentPage);
							break;
						}
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
			return this.fragments.get(postion);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	private void displayTestwiseMarks() {
		mSubjectwiseViewPager.setVisibility(View.GONE);
		mTestwiseViewPager.setVisibility(View.VISIBLE);
		mTestwiseViewPager.removeAllViews();
		List<Fragment> fragments = getFragments(mTestwiseSpareArrayList.size(),
				mTestwiseSpareArrayList);
		MyPageAdapter pageAdapter = new MyPageAdapter(
				getSupportFragmentManager(), fragments);
		pageAdapter.notifyDataSetChanged();
		mTestwiseViewPager.setAdapter(pageAdapter);
	}

	private void displaySubjectwiseMarks() {
		mTestwiseViewPager.setVisibility(View.GONE);
		mSubjectwiseViewPager.removeAllViews();
		mSubjectwiseViewPager.setVisibility(View.VISIBLE);
		List<Fragment> fragments = getFragments(
				mSubjectwiseSparseArrayList.size(), mSubjectwiseSparseArrayList);
		MyPageAdapter pageAdapter = new MyPageAdapter(
				getSupportFragmentManager(), fragments);
		pageAdapter.notifyDataSetChanged();
		mSubjectwiseViewPager.setAdapter(pageAdapter);
	}

	private List<Fragment> getFragments(int count,
			List<SparseArray<Groupmarks>> sparseArrayList) {
		List<Fragment> fList = new ArrayList<Fragment>();
		for (int i = 0; i < count; i++) {
			fList.add(MyFragment.newInstance(sparseArrayList.get(i)));
		}
		return fList;
	}

	class FetchJson extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			mLoadingProgressBar.setVisibility(View.VISIBLE);
			URL url;
			HttpURLConnection urlconnection = null;
			InputStream in = null;
			String result = null;
			JSONArray json;
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

			} catch (Exception e) {
				return null;
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			mLoadingProgressBar.setVisibility(View.INVISIBLE);
			if (result != null) {
				if(parseMarksJSON(result)==true)
				{	
				displaySubjectwiseMarks();
				mTotalPages = mSubjectwiseSparseArrayList.size();
				mCurrentPage = mSubjectwiseViewPager.getCurrentItem();
				updateIndicator(mCurrentPage);
				}
			}
		}
	}

	private boolean parseMarksJSON(String result) {
		JSONObject marksJSON;
		try {
			marksJSON = new JSONObject(result);
			if(!marksJSON.getString("status").equals("200"))//mark entry not available
			{
				Toast.makeText(this, marksJSON.getString("error"), Toast.LENGTH_LONG).show();
				return false;
			}
			else
			{
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
				if (!mIndex.isEmpty())
					mIndex.clear();
				if (!mGroupList.isEmpty())
					mGroupList.clear();
				mSubjectToMarks = new HashMap<String, Map<String, String>>();
				for (int i = 0; i < jsontable.length(); i++) {
					jsonrow = jsontable.getJSONArray(i);
					for (int j = 0; j < jsonrow.length(); j++) {
						if (i == 0) {
							mIndex.add(jsonrow.getString(j));
						} else if (i > 0 && j == 0) {
							mGroupList.add(jsonrow.getString(j));
						}
					}
				}
				for (int i = 1; i < jsontable.length(); i++) {
					jsonrow = jsontable.getJSONArray(i);
					subjectmarktemp = new HashMap<String, String>();
					for (int j = 1; j < jsonrow.length(); j++) {
						subjectmarktemp
								.put(mIndex.get(j), jsonrow.getString(j));
					}
					mSubjectToMarks.put(mGroupList.get(i - 1), subjectmarktemp);
				}

				Groupmarks testwisemarks;
				JSONArray indexjsonarray = new JSONArray();
				indexjsonarray = jsontable.getJSONArray(0);
				Marks marksobject = new Marks();
				mTestwiseSparseArray = new SparseArray<Groupmarks>();
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
					mTestwiseSparseArray.append(i - 1, testwisemarks);
				}
				mTestwiseSpareArrayList.add(mTestwiseSparseArray);
				Log.d("DEBUG", "testwisesparsearraylist: "
						+ mTestwiseSpareArrayList.size());

				mSubjectwiseSparseArray = new SparseArray<Groupmarks>();
				Marks temp;
				Map<String, String> tempmap = new HashMap<String, String>();
				Groupmarks groupMarks = null;
				int i = 0;
				for (Entry entry : mSubjectToMarks.entrySet()) {

					groupMarks = new Groupmarks((String) entry.getKey());
					temp = new Marks();
					tempmap = mSubjectToMarks.get(entry.getKey());
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
				Log.d("DEBUG", "subjectwisesparsearraylist: "
						+ mSubjectwiseSparseArrayList.size());
			}
			
			}//end of first else part
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
		
	}

	public void updateIndicator(int currentPage) {
		mDotsScrollBarHolder.removeAllViews();
		DotsScrollBar.createDotScrollBar(this, mDotsScrollBarHolder,
				mCurrentPage, mTotalPages);
		mDotsScrollBarHolder.bringToFront();
	}

}