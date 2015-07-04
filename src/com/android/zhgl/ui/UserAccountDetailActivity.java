package com.android.zhgl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.zhgl.Constant;
import com.android.zhgl.DBGUtils;
import com.android.zhgl.R;
import com.android.zhgl.UserAccount;
import com.android.zhgl.database.ZHGLBuffer;

public class UserAccountDetailActivity extends Activity{

	private UserAccount mUserAccount;
	private TextView mTvName;
	private TextView mTvId;
	private TextView mTvPassword;
	private TextView mTvNote;
	
	private ActionBarMain mActionBarMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_account_detail);
		
		mUserAccount = (UserAccount) getIntent().getSerializableExtra(Constant.USER_ACCOUNT);
		if(mUserAccount == null){
			finish();
			return;
		}
		DBGUtils.menuPrint("mUserAccount addr:" + mUserAccount);
		
		findViews();
		
		initViews();
	}
	
	private void findViews(){
		mTvName = (TextView)findViewById(R.id.TextView_Account_Name);
		mTvId = (TextView)findViewById(R.id.TextView_Account_Id);
		mTvPassword = (TextView)findViewById(R.id.TextView_Account_Password);
		mTvNote = (TextView)findViewById(R.id.TextView_Account_Note);
	}
	
	private void initViews(){
		setViews(mUserAccount);
		mActionBarMain = new ActionBarMain(this,R.string.edit);
		mActionBarMain.setSettingClickListener(mClickListener);
	}
	
	private void setViews(UserAccount account){
		mTvName.setText(account.mName);
		mTvId.setText(account.mIdentification);
		mTvPassword.setText(account.mPassword);
		mTvNote.setText(account.mNote);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			UserAccount resultUserAccount = (UserAccount) data
					.getSerializableExtra(Constant.USER_ACCOUNT);
			if(resultUserAccount == null){
				DBGUtils.menuErrPrint("[onActivityResult] object null.");
				return;
			}
			ZHGLBuffer buffer = ZHGLBuffer.getInstance();
			UserAccount userAccount = buffer.findUserAccount(mUserAccount);
			if(userAccount != null){
				userAccount.copy(resultUserAccount);
				DBGUtils.menuPrint("[onActivityResult]update user account.");
			}else{
				DBGUtils.menuErrPrint("[onActivityResult]add user account.");
				buffer.getUserAccounts().add(resultUserAccount);
			}
			mUserAccount.copy(resultUserAccount);
			setViews(mUserAccount);
			break;
		case RESULT_CANCELED:
			
			break;
		default:
			break;
		}
	}
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.IDC_Button_ActionBar_Setting:
				Intent intent = new Intent(UserAccountDetailActivity.this, UserAccountEditActivity.class);
				intent.putExtra(Constant.USER_ACCOUNT, mUserAccount);
				UserAccountDetailActivity.this.startActivityForResult(intent,0);
				break;
			default:
				break;
			}
			
		}
	};
}
