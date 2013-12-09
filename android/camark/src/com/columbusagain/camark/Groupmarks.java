package com.columbusagain.camark;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Groupmarks implements Parcelable {
	String subjectname;
	
	List<Marks> marks = new ArrayList<Marks>();

	public Groupmarks(String subject) {
		// TODO Auto-generated constructor stub
		subjectname = subject;

	}

	protected Groupmarks(Parcel in) {
		subjectname = in.readString();
		marks = new ArrayList<Marks>();
		in.readList(marks, null);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(subjectname);
		dest.writeList(marks);
	}

	public static final Parcelable.Creator<Groupmarks> CREATOR = new Parcelable.Creator<Groupmarks>() {
		public Groupmarks createFromParcel(Parcel in) {
			return new Groupmarks(in);
		}

		public Groupmarks[] newArray(int size) {
			return new Groupmarks[size];
		}
	};
}
