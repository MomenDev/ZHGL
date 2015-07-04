package com.android.zhgl.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.zhgl.R;

public class ActionBarMain {
	private final TextView mTvActionBarTitle;
	private final Button mBtActionBarMenu;
	private final Button mBtActionBarSetting;
	private final ActionBar mActionBar;
	private final Activity mActivity;
	
	public ActionBarMain(Activity activity,int settingTextId) {
		super();
		mActivity = activity;
		mActionBar = mActivity.getActionBar();
		mActionBar.setCustomView(R.layout.actionbar_style_main);
		mActionBar.setDisplayShowCustomEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		View actionbarView = mActionBar.getCustomView();
		mTvActionBarTitle = (TextView)actionbarView.findViewById(R.id.IDC_TextView_ActionBar_title);
		mBtActionBarMenu = (Button)actionbarView.findViewById(R.id.IDC_Button_ActionBar_Options);
		mBtActionBarSetting = (Button)actionbarView.findViewById(R.id.IDC_Button_ActionBar_Setting);
	
		mBtActionBarSetting.setText(settingTextId);
	}
	
	public void setMenuClickListener(OnClickListener listener){
		mBtActionBarMenu.setOnClickListener(listener);
	}
	
	public void setSettingClickListener(OnClickListener listener){
		mBtActionBarSetting.setOnClickListener(listener);
	}
	
	public void updateTitle(CharSequence title) {
		if(title == null){
			return;
		}
		mTvActionBarTitle.setText(title);
	}
	
	public void updateTitle(int resId) {
		mTvActionBarTitle.setText(resId);
	}
}
