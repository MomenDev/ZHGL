package com.android.zhgl;

import java.io.Serializable;


/**
 * @author Administrator
 * @version 1.0
 * @created 14-05-2014 22:33:02
 */
public class UserAccount implements Serializable{

	private static final long serialVersionUID = -7519425335291825823L;
	public static final String ID = "Identification";
	public static final String NAME = "Name";
	public static final String NOTE = "Note";
	public static final String PASSWORD = "Password";
	
	public String mName;
	public String mIdentification;
	public String mPassword;
	public String mNote;

	public boolean mChecked = false;
	
	public UserAccount(){
		mName = "";
		mIdentification = "";
		mPassword = "";
		mNote = "";
	}

	public UserAccount(String name, String identification, String password,
			String note) {
		super();
		mName = name;
		mIdentification = identification;
		mPassword = password;
		mNote = note;
	}
	
	public void copy(UserAccount account){
		mName = account.mName;
		mIdentification = account.mIdentification;
		mPassword = account.mPassword;
		mNote = account.mNote;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccount other = (UserAccount) obj;
		if (mIdentification == null) {
			if (other.mIdentification != null)
				return false;
		} else if (!mIdentification.equals(other.mIdentification))
			return false;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		if (mNote == null) {
			if (other.mNote != null)
				return false;
		} else if (!mNote.equals(other.mNote))
			return false;
		if (mPassword == null) {
			if (other.mPassword != null)
				return false;
		} else if (!mPassword.equals(other.mPassword))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserAccount [mName=" + mName + ", mIdentification="
				+ mIdentification + ", mPassword=" + mPassword + ", mNote="
				+ mNote + "]";
	}
}