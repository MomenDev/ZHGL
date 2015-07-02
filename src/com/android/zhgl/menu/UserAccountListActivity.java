package com.android.zhgl.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.zhgl.Constant;
import com.android.zhgl.DBGUtils;
import com.android.zhgl.R;
import com.android.zhgl.UserAccount;
import com.android.zhgl.database.ZHGLBuffer;

/**
 * @author Administrator
 * @version 1.0
 * @created 14-05-2014 22:33:02
 */
public class UserAccountListActivity extends Activity {

	private ListView mListView;
	private UserAccountAdapter mAccountAdapter;

	private ActionBarMain mActionBarMain;
	private Button mBtAccountAdd;
	private Button mBtAccountDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_account_list_layout);

		findViews();

		initViews();
	}

	private void findViews() {
		mListView = (ListView) findViewById(R.id.ListView_AccountList);

		mBtAccountAdd = (Button) findViewById(R.id.Button_Account_Add);
		mBtAccountDelete = (Button) findViewById(R.id.Button_Account_Delete);

	}

	private void initViews() {
		mAccountAdapter = new UserAccountAdapter(this);
		mListView.setAdapter(mAccountAdapter);
		mListView.setOnItemClickListener(mAccountAdapter);

		mActionBarMain = new ActionBarMain(this, R.string.sync);
		mActionBarMain.setSettingClickListener(mClickListener);

		mBtAccountAdd.setOnClickListener(mAccountClickListener);
		mBtAccountDelete.setOnClickListener(mAccountClickListener);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.IDC_Button_ActionBar_Setting:
				ZHGLBuffer.getInstance().syncDatabase();
				mAccountAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}

		}
	};

	private OnClickListener mAccountClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.Button_Account_Add:
				Intent intent = new Intent(UserAccountListActivity.this,
						UserAccountEditActivity.class);
				UserAccountListActivity.this.startActivityForResult(intent, 0);
				break;
			case R.id.Button_Account_Delete:
				ZHGLBuffer buffer = ZHGLBuffer.getInstance();
				if (buffer.getCheckedusetAccountNum() <= 0) {
					Toast.makeText(UserAccountListActivity.this,
						R.string.alert_delete_empty, Toast.LENGTH_LONG)
						.show();
					return;
				}

				new AlertDialog.Builder(UserAccountListActivity.this)
					.setMessage(R.string.alert_delete)
					.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ZHGLBuffer buffer = ZHGLBuffer
										.getInstance();
								buffer.removeCheckedUserAccount();
								mAccountAdapter.notifyDataSetChanged();
							}
						}).setNegativeButton(R.string.cancel, null)
					.show();
				break;
			default:
				break;
			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			UserAccount resultUserAccount = (UserAccount) data
					.getSerializableExtra(Constant.USER_ACCOUNT);
			if (resultUserAccount == null) {
				DBGUtils.menuErrPrint("[onActivityResult] object null.");
				return;
			}
			ZHGLBuffer buffer = ZHGLBuffer.getInstance();
			buffer.getUserAccounts().add(resultUserAccount);
			mAccountAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		mAccountAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ZHGLBuffer.getInstance().syncDatabase();
		mAccountAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}