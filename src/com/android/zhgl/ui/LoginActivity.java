package com.android.zhgl.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.gesturelocker.ui.GestureLockActivity;
import com.android.zhgl.R;
import com.android.zhgl.database.ZHGLBuffer;
import com.android.zhgl.security.AESUtils;
import com.android.zhgl.security.ZHGLPreferences;

public class LoginActivity extends Activity {

	Button btnLogin;
	EditText etAccountId;
	EditText etAccountPassword;

	public static final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		btnLogin = (Button) findViewById(R.id.Button_Account_Login);
		etAccountId = (EditText) findViewById(R.id.EditText_Account_Id);
		etAccountPassword = (EditText) findViewById(R.id.EditText_Account_Password);
		String md5Password = ZHGLPreferences.getPreference(this,
				ZHGLPreferences.GESTURE_PASSWORD);
		if (md5Password != null && !md5Password.equals("")) {
			Intent intent = new Intent();
			intent.setClass(this, GestureLockActivity.class);
			intent.putExtra(GestureLockActivity.KEY_MODE,
					GestureLockActivity.MODE_CONFIRM_PASSWORD);
			intent.putExtra(GestureLockActivity.KEY_ORIGINAL_MD5_PASSWORD,
					md5Password);
			startActivityForResult(intent, REQUEST_CODE);
		}

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkZhglAccount();
			}
		});
	}

	private void checkZhglAccount() {
		final String id = etAccountId.getText().toString();
		if (id.equals("")) {
			Toast.makeText(this, R.string.logoin_name_should_not_empty,
					Toast.LENGTH_LONG).show();
			return;
		}
		final String password = etAccountPassword.getText().toString();
		if (password.equals("")) {
			Toast.makeText(this, R.string.logoin_password_should_not_empty,
					Toast.LENGTH_LONG).show();
			return;
		}
		final ZHGLBuffer zhglBuffer = ZHGLBuffer.getInstance();
		if (!zhglBuffer.readZhglFromStorage()) {
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.account_not_exist);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							zhglBuffer.mZhglAccount.autoLogin(id, password);
							Intent intent = new Intent(LoginActivity.this,
									UserAccountListActivity.class);
							LoginActivity.this.startActivity(intent);
							zhglBuffer.loadDatabase();
						}
					});
			builder.setNegativeButton(R.string.cancel, null).show();
			return;
		}

		if (!zhglBuffer.mZhglAccount.login(id, password)) {
			Toast.makeText(this, R.string.logoin_error, Toast.LENGTH_LONG)
					.show();
			return;
		}

		zhglBuffer.loadDatabase();

		Intent intent = new Intent(LoginActivity.this,
				UserAccountListActivity.class);
		LoginActivity.this.startActivity(intent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode != REQUEST_CODE)
			return;

		if (resultCode != RESULT_OK)
			return;

		int mode = data.getIntExtra(GestureLockActivity.KEY_MODE, 0);
		String gessturePassword = data
				.getStringExtra(GestureLockActivity.KEY_PASSWORD);

		if (mode != GestureLockActivity.MODE_CONFIRM_PASSWORD)
			return;

		String encryptId = ZHGLPreferences.getPreference(LoginActivity.this,
				ZHGLPreferences.ORIGINAL_NAME);
		String encryptPassword = ZHGLPreferences.getPreference(
				LoginActivity.this, ZHGLPreferences.ORIGINAL_PASSWORD);

		String id = AESUtils.decrypt(gessturePassword, encryptId);
		String password = AESUtils.decrypt(gessturePassword, encryptPassword);

		if (id == null || id.equals(""))
			return;

		if (password == null || password.equals(""))
			return;

		final ZHGLBuffer zhglBuffer = ZHGLBuffer.getInstance();

		if (!zhglBuffer.readZhglFromStorage())
			return;

		if (!zhglBuffer.mZhglAccount.login(id, password))
			return;

		zhglBuffer.loadDatabase();

		Intent intent = new Intent(LoginActivity.this,
				UserAccountListActivity.class);
		LoginActivity.this.startActivity(intent);

		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// etAccountId.setText("");
		etAccountPassword.setText("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
