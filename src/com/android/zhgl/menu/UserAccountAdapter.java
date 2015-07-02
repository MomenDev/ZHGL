package com.android.zhgl.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.zhgl.Constant;
import com.android.zhgl.DBGUtils;
import com.android.zhgl.R;
import com.android.zhgl.UserAccount;
import com.android.zhgl.ZHGLAccount;
import com.android.zhgl.database.ZHGLBuffer;

public class UserAccountAdapter extends BaseAdapter implements OnItemClickListener{

	private Context mContext;
	private LayoutInflater mInflater;
	private ZHGLBuffer mZhglBuffer;
	
	public UserAccountAdapter(Context context) {
		super();
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mZhglBuffer = ZHGLBuffer.getInstance();
	}

	@Override
	public int getCount() {
		return mZhglBuffer.getUserAccounts().size();
	}

	@Override
	public Object getItem(int arg0) {
		return mZhglBuffer.getUserAccounts().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.user_account_list_item, null);
			TextView mTvUserAccountName = (TextView) convertView.findViewById(R.id.TextView_AccountName);
			CheckBox mCbAccountCheck = (CheckBox)convertView.findViewById(R.id.CheckBox_AccountCheck);
			holder = new ViewHolder(mTvUserAccountName,mCbAccountCheck);
			convertView.setTag(holder);
			holder.mCbAccountCheck.setOnCheckedChangeListener(mCheckedChangeListener);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		UserAccount account = mZhglBuffer.getUserAccounts().get(position);
		holder.mCbAccountCheck.setTag(position);
		holder.mTvUserAccountName.setText(account.mName);
		holder.mCbAccountCheck.setChecked(account.mChecked);
		return convertView;
	}

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Integer position = (Integer) buttonView.getTag();
			if(position == null){
				DBGUtils.menuErrPrint("[onCheckedChanged] error !");
				return;
			}
			setCheckItem(position, isChecked);
		}
	};
	
	 private void setCheckItem(int position,boolean checked){
		UserAccount account = mZhglBuffer.getUserAccounts().get(position);
		account.mChecked = checked;
	}
	
	private class ViewHolder{
		TextView mTvUserAccountName;
		CheckBox mCbAccountCheck;
		
		public ViewHolder(TextView tvUserAccountName, CheckBox cbAccountCheck) {
			super();
			mTvUserAccountName = tvUserAccountName;
			mCbAccountCheck = cbAccountCheck;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int positon, long arg3) {
		UserAccount userAccount = (UserAccount) getItem(positon);
		Intent intent = new Intent(mContext, UserAccountDetailActivity.class);
		intent.putExtra(Constant.USER_ACCOUNT, userAccount);
		mContext.startActivity(intent);
	}
}
