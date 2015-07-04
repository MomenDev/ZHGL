package com.android.zhgl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.gesturelocker.common.MD5;
import com.android.gesturelocker.ui.GestureLockActivity;
import com.android.zhgl.R;
import com.android.zhgl.ZHGLAccount;
import com.android.zhgl.database.ZHGLBuffer;
import com.android.zhgl.security.AESUtils;
import com.android.zhgl.security.ZHGLPreferences;

public class SettingActivity extends Activity {

	private Button mBtAddGesturePassword;

	public static final int REQUEST_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seting_layout);

		mBtAddGesturePassword = (Button) findViewById(R.id.setting_bt_add_gesture_password);

		mBtAddGesturePassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(SettingActivity.this,
						GestureLockActivity.class);
				intent.putExtra(GestureLockActivity.KEY_MODE,
						GestureLockActivity.MODE_INIT_PASSWORD);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode != REQUEST_CODE)
			return;

		if (resultCode != RESULT_OK)
			return;

		int mode = data.getIntExtra(GestureLockActivity.KEY_MODE, 0);

		if (mode != GestureLockActivity.MODE_INIT_PASSWORD)
			return;
		ZHGLAccount zhglAccount = ZHGLBuffer.getInstance().mZhglAccount;
		String gesturePassword = data.getStringExtra(GestureLockActivity.KEY_PASSWORD);
		String encryptId = AESUtils.encrypt(gesturePassword, zhglAccount.mIdentification);
		String encryptPassword = AESUtils.encrypt(gesturePassword, zhglAccount.mPassword);
		ZHGLPreferences.setPreference(this, ZHGLPreferences.GESTURE_PASSWORD, MD5.encrypt(gesturePassword));
		ZHGLPreferences.setPreference(this, ZHGLPreferences.ORIGINAL_NAME, encryptId);
		ZHGLPreferences.setPreference(this, ZHGLPreferences.ORIGINAL_PASSWORD, encryptPassword);
	}

}
