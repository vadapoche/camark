package com.vadapoche.camark;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vadapoche.camark.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String, Map<String, String>> subjectmarks;
	private List<String> subject;

	private SparseArray<Groupmarks> mGroups;

	public ExpandableListAdapter(Activity context,
			SparseArray<Groupmarks> mGroups) {
		this.context = context;
		this.subjectmarks = subjectmarks;
		this.subject = subject;
		this.mGroups = mGroups;
	}

	@Override
	public Marks getChild(int groupposition, int childposition) {
		// TODO Auto-generated method stub
		//Log.d("DEBUG", "debug: " + groupposition + ' ' + childposition);
		return mGroups.get(groupposition).marks.get(childposition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Marks marks = getChild(groupPosition, childPosition);

		Map<String, String> child = marks.child;

		Log.d("Marksss", marks.child.toString());

		// final String subject=(String)getChild(groupPosition, childPosition);
		LayoutInflater inflater = context.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child, null);
		}
		LinearLayout linearLayout = (LinearLayout) convertView
				.findViewById(R.id.childLayout);
		linearLayout.removeAllViews();
		View v;
		for (Entry entry : child.entrySet()) {
			v = inflater.inflate(R.layout.child_item, linearLayout, false);
			TextView textView;
			textView = (TextView) v.findViewById(R.id.test);
			textView.setText(entry.getKey().toString());
			textView = (TextView) v.findViewById(R.id.mark);
			textView.setText(entry.getValue().toString());
			linearLayout.addView(v);
		}

		TextView subjectview = (TextView) convertView
				.findViewById(R.id.subject);
		// subjectview.setText(subject);
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mGroups.get(groupPosition).marks.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String subjectname = (String) mGroups.get(groupPosition).subjectname;
		//Log.d("DEBUG", subjectname);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.subject);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(subjectname);
		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
