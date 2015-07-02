package com.android.zhgl.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.zhgl.R;
import com.android.zhgl.database.ZHGLBuffer;

public class LoginActivity extends Activity{

	Button btnLogin;
	EditText etAccountId;
	EditText etAccountPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		btnLogin = (Button)findViewById(R.id.Button_Account_Login);
		etAccountId = (EditText)findViewById(R.id.EditText_Account_Id);
		etAccountPassword = (EditText)findViewById(R.id.EditText_Account_Password);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkZhglAccount();
			}
		});
	}
	
	private void checkZhglAccount(){
		final String id = etAccountId.getText().toString();
		if(id.equals("")){
			Toast.makeText(this, R.string.logoin_name_should_not_empty, Toast.LENGTH_LONG).show();
			return;
		}
		final String password = etAccountPassword.getText().toString();
		if(password.equals("")){
			Toast.makeText(this, R.string.logoin_password_should_not_empty, Toast.LENGTH_LONG).show();
			return;
		}
		final ZHGLBuffer zhglBuffer = ZHGLBuffer.getInstance();
		if(!zhglBuffer.readZhglFromStorage()){
			new AlertDialog.Builder(this)
			.setMessage(R.string.account_not_exist)
			.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					zhglBuffer.mZhglAccount.autoLogin(id, password);
					Intent intent = new Intent(LoginActivity.this, UserAccountListActivity.class);
					LoginActivity.this.startActivity(intent);
					zhglBuffer.loadDatabase();
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.show();
			return;
		}
		
		if(!zhglBuffer.mZhglAccount.login(id, password)){
			Toast.makeText(this, R.string.logoin_error, Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(LoginActivity.this, UserAccountListActivity.class);
		LoginActivity.this.startActivity(intent);
		zhglBuffer.loadDatabase();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		etAccountId.setText("");
		etAccountPassword.setText("");
		ZHGLBuffer.getInstance().mZhglAccount.logout();
	}
}
