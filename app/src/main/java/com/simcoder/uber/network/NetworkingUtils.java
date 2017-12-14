package com.simcoder.uber.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.simcoder.uber.AppController;


public class NetworkingUtils {

	/**
	 * A type which indicates that the device is currently not connected to any network
	 */
	public static int TYPE_NOT_CONNECTED = 0;
	/** A type which indicates that the device is connected via Wi-Fi */
	public static int TYPE_WIFI = 1;
	/** A type which indicates that the device is connected via Mobile Data */
	public static int TYPE_MOBILE = 2;

	/**
	 * Indicates whether the device is connected to a network, doesn't
	 * necessarily mean that Internet is present
	 * 
	 * @return True if the device is connected, false otherwise
	 */
	public static boolean isNetworkConnected() {
		ConnectivityManager connectivity = (ConnectivityManager) AppController.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	/**
	 * Returns current state of the network as a type of these [
	 * {@link NetworkingUtils#TYPE_MOBILE} or {@link NetworkingUtils#TYPE_WIFI}
	 * or {@link NetworkingUtils#TYPE_NOT_CONNECTED} ]
	 * 
	 * @return
	 */
	public static int getConnectivityStatus() {
		ConnectivityManager cm = (ConnectivityManager) AppController.getInstance()
				.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

}
