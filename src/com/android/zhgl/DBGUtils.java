package com.android.zhgl;

import android.util.Log;

public class DBGUtils {

	public static final boolean MENU = false;
	public static final boolean MENUERR = true;
	
	public static void menuPrint(Object object){
		if(MENU){Log.d("Menu", object.toString());}
	}
	public static void menuErrPrint(Object object){
		if(MENUERR){Log.e("Menu", object.toString());}
	}
	
}
