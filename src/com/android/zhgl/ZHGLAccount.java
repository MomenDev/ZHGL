package com.android.zhgl;

import java.io.Serializable;

import com.android.zhgl.database.ZHGLBuffer;
import com.android.zhgl.security.AESUtils;


/**
 * @author Administrator
 * @version 1.0
 * @created 14-05-2014 22:33:02
 */
public class ZHGLAccount implements Serializable{

	private static final long serialVersionUID = -888146156251728494L;
	public static final String NAME = "Name";
	public static final String ID = "Identification";
	public static final String PASSWORD = "Password";
	public static final String NOTE = "Note";
	
	public String mIdentification;
	public String mPassword;
	public String mEncryptedCode;

	
	public ZHGLAccount() {
		super();
		mIdentification = null;
		mPassword = null;
	}
	
	public void loadEncryptCode(String encryptedCode){
		mEncryptedCode = encryptedCode;
	}

	public void autoLogin(String identification, String password) {
		mIdentification = identification;
		mPassword = password;
		String seed = password;
		String clearText = mIdentification;
		mEncryptedCode = AESUtils.encrypt(seed, clearText);
	}
	
	public boolean login(String id,String password){
		String seed = password;
		String clearText = id;
		String encryptCode = AESUtils.encrypt(seed, clearText);
		if(mEncryptedCode.equals(encryptCode)){
			mIdentification = id;
			mPassword = password;
			return true;
		}else{
			return false;
		}
		
	}

	public boolean logout(){
		mIdentification = null;
		mPassword = null;
		mEncryptedCode = null;
		ZHGLBuffer.getInstance().getUserAccounts().clear();
		return true;
	}

	@Override
	public String toString() {
		return "ZHGLAccount [mIdentification=" + mIdentification
				+ ", mPassword=" + mPassword + ", mEncryptedCode="
				+ mEncryptedCode + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZHGLAccount other = (ZHGLAccount) obj;
		if (mIdentification == null) {
			if (other.mIdentification != null)
				return false;
		} else if (!mIdentification.equals(other.mIdentification))
			return false;
		if (mPassword == null) {
			if (other.mPassword != null)
				return false;
		} else if (!mPassword.equals(other.mPassword))
			return false;
		return true;
	}
	
	

}