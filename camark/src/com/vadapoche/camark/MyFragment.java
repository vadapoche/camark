package com.vadapoche.camark;

import android.app.ExpandableListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import com.vadapoche.camark.R;

public class MyFragment extends Fragment {

	static SparseArray<Groupmarks> sparseArrayGroups;

	ExpandableListAdapter explistadapter;

	public static final MyFragment newInstance(
			SparseArray<Groupmarks> groupMarks) {
		MyFragment myfargment = new MyFragment();
		Bundle bundle = new Bundle();
		bundle.putSparseParcelableArray("sparse_array",
				(SparseArray<? extends Parcelable>) groupMarks);
		myfargment.setArguments(bundle);
		return myfargment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_expandable_list, container,
				false);

		sparseArrayGroups = getArguments().getSparseParcelableArray(
				"sparse_array");

		ExpandableListView expListView = (ExpandableListView) v
				.findViewById(R.id.mark_list);
		explistadapter = new com.vadapoche.camark.ExpandableListAdapter(
				getActivity(), sparseArrayGroups);
		expListView.setAdapter(explistadapter);
		return v;

	}
}
