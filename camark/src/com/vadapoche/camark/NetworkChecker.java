package com.vadapoche.camark;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {
	public static boolean isConnected(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null)
				return false;
			return info.isConnected();
		} catch (Exception e) {
			return false;
		}
	}
}
