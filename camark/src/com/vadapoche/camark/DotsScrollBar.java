package com.vadapoche.camark;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vadapoche.camark.R;

public class DotsScrollBar {

	LinearLayout main_image_holder;

	public static void createDotScrollBar(Context context,
			LinearLayout main_holder, int selectedPage, int count) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("Jiffle", ""+count+"  "+selectedPage);
		for (int i = 0; i < count; i++) {

			View view = inflater.inflate(R.layout.dot, main_holder, false);

			ImageView dotImageView = (ImageView) view
					.findViewById(R.id.dotImage);
			if (i == selectedPage) {
				try {
					dotImageView.setImageResource(R.drawable.selected_page);
				} catch (Exception e) {
					Log.d("inside DotsScrollBar.java",
							"could not locate identifier");
				}
			} else {
				dotImageView.setImageResource(R.drawable.unselected_page);
			}
			main_holder.addView(view);
		}
		// main_holder.invalidate();
	}
}
