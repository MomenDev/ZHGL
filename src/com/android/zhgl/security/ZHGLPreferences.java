package com.android.zhgl.security;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ZHGLPreferences {

	public static final String GESTURE_DATA = "GestureData";

	public static final String GESTURE_PASSWORD = "GesturePassword";
	
	public static final String ORIGINAL_NAME = "OriginalName";
	
	public static final String ORIGINAL_PASSWORD = "OriginalPassword";

	public static void setPreference(Context context, String key, String value) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				GESTURE_DATA, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPreference(Context context, String key) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				GESTURE_DATA, Activity.MODE_PRIVATE);
		String value = mySharedPreferences.getString(key, "");
		return value;
	}
}
