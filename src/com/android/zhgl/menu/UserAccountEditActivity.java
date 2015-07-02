package com.android.zhgl.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.zhgl.Constant;
import com.android.zhgl.DBGUtils;
import com.android.zhgl.R;
import com.android.zhgl.UserAccount;

public class UserAccountEditActivity extends Activity{

	private UserAccount mUserAccount;
	private EditText mTvName;
	private EditText mTvId;
	private EditText mTvPassword;
	private EditText mTvNote;
	
	private Button mBtAccountSave;
	private Button mBtAccountCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_account_edit);
		
		mUserAccount = (UserAccount) getIntent().getSerializableExtra(Constant.USER_ACCOUNT);
		if(mUserAccount == null){
			mUserAccount = new UserAccount();
		}
		
		DBGUtils.menuPrint("mUserAccount addr:" + mUserAccount);
		
		findViews();
		
		initViews();
	}
	
	private void findViews(){
		mTvName = (EditText)findViewById(R.id.EditText_Account_Name);
		mTvId = (EditText)findViewById(R.id.EditText_Account_Id);
		mTvPassword = (EditText)findViewById(R.id.EditText_Account_Password);
		mTvNote = (EditText)findViewById(R.id.EditText_Account_Note);
		mBtAccountSave = (Button)findViewById(R.id.Button_Account_Save);
		mBtAccountCancel = (Button)findViewById(R.id.Button_Account_Cancel);
	}
	
	private void initViews(){
		mTvName.setText(mUserAccount.mName);
		mTvId.setText(mUserAccount.mIdentification);
		mTvPassword.setText(mUserAccount.mPassword);
		mTvNote.setText(mUserAccount.mNote);
		
		mBtAccountSave.setOnClickListener(mClickListener);
		mBtAccountCancel.setOnClickListener(mClickListener);
	}
	
	public boolean validCheck(){
		String s = null;
		s = mTvName.getText().toString();
		if(s == null || s.equals("")){
			Toast.makeText(this, R.string.account_name_invalid, Toast.LENGTH_LONG).show();
			return false;
		}
		
		s = mTvId.getText().toString();
		if(s == null || s.equals("")){
			Toast.makeText(this, R.string.account_id_invalid, Toast.LENGTH_LONG).show();
			return false;
		}
		
		s = mTvPassword.getText().toString();
		if(s == null || s.equals("")){
			Toast.makeText(this, R.string.account_password_invalid, Toast.LENGTH_LONG).show();
			return false;
		}
		
		s = mTvNote.getText().toString();
		if(s == null || s.equals("")){
			Toast.makeText(this, R.string.account_note_invalid, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private void setAccountResult( UserAccount account){
		account.mName = mTvName.getText().toString();
		account.mIdentification = mTvId.getText().toString();
		account.mPassword = mTvPassword.getText().toString();
		account.mNote = mTvNote.getText().toString();
	}
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.Button_Account_Save:
				if(!validCheck()){
					return;
				}
				setAccountResult(mUserAccount);
				Intent intent = new Intent();
				intent.putExtra(Constant.USER_ACCOUNT, mUserAccount);
				DBGUtils.menuPrint("UserAccount:" + mUserAccount);
				setResult(RESULT_OK, intent);
				finish();
				break;
			case R.id.Button_Account_Cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;
			default:
				break;
			}
			
		}
	};

}
