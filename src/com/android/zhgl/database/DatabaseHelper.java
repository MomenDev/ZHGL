package com.android.zhgl.database;

import com.android.zhgl.UserAccount;
import com.android.zhgl.ZHGLAccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	public static final String ZHGL_ACCOUNT_TABLE = "ZHGLAccount";
	public static final String USER_ACCOUNT_TABLE = "UserAccount";
	
	public static final int DATABASE_VER = 1;
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public DatabaseHelper(Context context,String name){
		this(context,name,DATABASE_VER);
	}
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				ZHGL_ACCOUNT_TABLE + "(" +
				ZHGLAccount.NAME + " text," +
				ZHGLAccount.ID + " text," + 
				ZHGLAccount.PASSWORD + " text,"+
				ZHGLAccount.NOTE + " text" +
				")"
			);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				USER_ACCOUNT_TABLE + "(" +
				UserAccount.NAME + " text," +
				UserAccount.ID + " text," + 
				UserAccount.PASSWORD + " text,"+
				UserAccount.NOTE + " text" +
				")"
			);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(" DROP TABLE IF EXISTS " + ZHGL_ACCOUNT_TABLE);
		db.execSQL(" DROP TABLE IF EXISTS " + USER_ACCOUNT_TABLE);
		onCreate(db);
	}

}
