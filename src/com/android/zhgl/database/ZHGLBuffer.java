package com.android.zhgl.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.android.zhgl.AESUtils;
import com.android.zhgl.DBGUtils;
import com.android.zhgl.UserAccount;
import com.android.zhgl.ZHGLAccount;

public class ZHGLBuffer {

	public static final String StorageDirPath;
	public static final String DirName = "ZHGL";
	public static final String FileName = "zhgldb.txt";
	public static final String FileNameProfix = "zhgldb-";
	
	public static final String ZHGL_PREFIX = "ZHGLS";
	public static final String ZHGL_SUFFIX = "ZHGLE";
	public static final String USER_PREFIX = "USERS";
	public static final String USER_SUFFIX = "USERE";
	public static final String SEPRATTOR = "T,T";
	
	static{
		StorageDirPath = Environment.getExternalStorageDirectory().getPath()
				+ File.separator + DirName;
	}
	
	private File mFile;
	
	private static ZHGLBuffer mZHGLBuffer = null;
	public final ZHGLAccount mZhglAccount;
	private final LinkedList<UserAccount> mUserAccounts;
	
	public static ZHGLBuffer getInstance(){
		if(mZHGLBuffer == null){
			mZHGLBuffer = new ZHGLBuffer();
		}
		return mZHGLBuffer;
	}
	
	private ZHGLBuffer(){
		mFile = new File(StorageDirPath + File.separator + FileName);
		if(!mFile.exists()){
			try {
				mFile.getParentFile().mkdirs();
				mFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mZhglAccount = new ZHGLAccount();
		mUserAccounts = new LinkedList<UserAccount>();
	}
	
	public UserAccount findUserAccount(UserAccount userAccount){
		UserAccount retAccount = null;
		
		for (UserAccount account : mUserAccounts) {
			if(userAccount.equals(account)){
				retAccount = account;
				break;
			}
		}
		
		return retAccount;
	}
		
	public LinkedList<UserAccount> getUserAccounts() {
		return mUserAccounts;
	}

	public void removeCheckedUserAccount(){
		int loopSize = mUserAccounts.size();
		for (int i = 0; i < loopSize; i++) {
			int size = mUserAccounts.size();
			for (int j = 0; j < size; j++) {
				UserAccount account = mUserAccounts.get(j);
				if(account.mChecked){
					mUserAccounts.remove(j);
					break;
				}
			}
		}
	}
	
	public int getCheckedusetAccountNum(){
		int checkedNum = 0;
		int size = mUserAccounts.size();
		for (int j = 0; j < size; j++) {
			if(mUserAccounts.get(j).mChecked){
				checkedNum++;
			}
		}
		return checkedNum;
	}
	
	public void resetUserAccountCheckedState(){
		int size = mUserAccounts.size();
		for (int j = 0; j < size; j++) {
			mUserAccounts.get(j).mChecked = false;
		}
	}
	
	public boolean readZhglFromStorage(){
		DBGUtils.menuPrint("readZhglFromStorage");
		BufferedReader bufferedReader = null;
		FileInputStream inputStream = null;
		boolean success = true;
		try {
			inputStream = new FileInputStream(mFile);
			InputStreamReader reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			if(!findZHGLAccount(bufferedReader)){
				DBGUtils.menuPrint("ZHGLAccount not found.");
				success = false;
			}
			DBGUtils.menuPrint("readZhglFromStorage end");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			inputStream.close();
			DBGUtils.menuPrint("close file");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Load database from the storage file.
	 * @return true for load database success,if there is no DB file,then return false.
	 */
	public boolean loadDatabase() {

		DBGUtils.menuPrint("loadDatabase");
		BufferedReader bufferedReader = null;
		FileInputStream inputStream = null;
		boolean success = true;
		try {
			inputStream = new FileInputStream(mFile);
			InputStreamReader reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			findUserAccount(bufferedReader);
			DBGUtils.menuPrint("loadDatabase end");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			inputStream.close();
			DBGUtils.menuPrint("close file");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean findString(BufferedReader reader,String s) throws IOException{
		DBGUtils.menuPrint("findString:" + s);
		String ret = reader.readLine();
		while(ret != null && !ret.equals(s)){
			ret = reader.readLine();
		}
		if(ret == null){
			DBGUtils.menuPrint("findString:" + s + ",fail !");
			return false;
		}else{
			DBGUtils.menuPrint("String was found:" + ret);
			return true;
		}
	}
	
	private boolean findZHGLAccount(BufferedReader reader) throws IOException{
		DBGUtils.menuPrint("step 1:findZHGLAccount");
		
		if(!findString(reader, ZHGL_PREFIX)){
			return false;
		}
		
		String ret = reader.readLine();
		boolean hasFoundZhglAccount = false;
		while(ret != null && !ret.equals(ZHGL_SUFFIX)){
			DBGUtils.menuPrint("find a String:" + ret);
			mZhglAccount.loadEncryptCode(ret);
			hasFoundZhglAccount = true;
			DBGUtils.menuPrint("find a ZHGLAccount:" + mZhglAccount);
			ret = reader.readLine();
		}
		return hasFoundZhglAccount;
	}
	
	private boolean findUserAccount(BufferedReader reader) throws IOException{
		DBGUtils.menuPrint("step2:findUserAccount");
		
		if(!findString(reader, USER_PREFIX)){
			return false;
		}
		
		String ret = reader.readLine();
		while(ret != null && !ret.equals(USER_SUFFIX)){
			DBGUtils.menuPrint("find a String:" + ret);
			String[] arr = ret.split(SEPRATTOR);
			if(arr.length < 4){
				DBGUtils.menuErrPrint("find a bad UserAccount:"+ret);
				ret = reader.readLine();
				continue;
			}
			String note = " ";
			if(arr.length >= 4){
				note = AESUtils.decrypt(mZhglAccount.mPassword, arr[3]);
			}
			String name = AESUtils.decrypt(mZhglAccount.mPassword, arr[0]);
			String identification = AESUtils.decrypt(mZhglAccount.mPassword, arr[1]);
			String password = AESUtils.decrypt(mZhglAccount.mPassword, arr[2]);
			UserAccount account = new UserAccount(name, identification, password,note);
			mUserAccounts.add(account);
			ret = reader.readLine();

			DBGUtils.menuPrint("find a UserAccount:" + account);
		}
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public void syncDatabase(){
		DBGUtils.menuPrint("syncDatabase");
		if(mZhglAccount.mPassword == null || mZhglAccount.mEncryptedCode == null){
			DBGUtils.menuErrPrint("syncDatabase Fail.");
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");
		String suffix = format.format(new Date());
		String path = StorageDirPath + File.separator + FileNameProfix + suffix;
		File destFile = new File(path);
		try {
			if(destFile.exists()){
				destFile.delete();
			}
			destFile.createNewFile();
			doCopyFile(mFile, destFile);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		FileOutputStream outputStream = null;
		BufferedWriter writer = null;
		try {
			outputStream = new FileOutputStream(mFile,false);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
			writer = new BufferedWriter(outputStreamWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		try {
			writeZHGLAccount(writer);
			writeUserAccount(writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void doCopyFile(File srcFile,File destFile) throws IOException{
		FileInputStream input = new FileInputStream(srcFile);
        try {
            FileOutputStream output = new FileOutputStream(destFile);
            try {
            	byte[] buffer = new byte[1024];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
            } finally {
            	try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ioe) {
                	ioe.printStackTrace();
                }
            }
        } finally {
        	try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ioe) {
            	ioe.printStackTrace();
            }
        }
	}
	
	private void writeZHGLAccount(BufferedWriter writer) throws IOException{
		StringBuffer buffer = new StringBuffer();
		writer.write(ZHGL_PREFIX);
		writer.newLine();
		buffer.append(mZhglAccount.mEncryptedCode);
		writer.write(buffer.toString());
		writer.newLine();
		writer.write(ZHGL_SUFFIX);
		writer.newLine();
	}
	
	private void writeUserAccount(BufferedWriter writer) throws IOException{
		StringBuffer buffer = null;
		writer.write(USER_PREFIX);
		writer.newLine();
		for (UserAccount account : mUserAccounts) {
			buffer =  new StringBuffer();
			buffer.append(AESUtils.encrypt(mZhglAccount.mPassword,account.mName) + SEPRATTOR);
			buffer.append(AESUtils.encrypt(mZhglAccount.mPassword,account.mIdentification) + SEPRATTOR);
			buffer.append(AESUtils.encrypt(mZhglAccount.mPassword,account.mPassword) + SEPRATTOR);
			buffer.append(AESUtils.encrypt(mZhglAccount.mPassword,account.mNote));
			writer.write(buffer.toString());
			writer.newLine();
		}
		writer.write(USER_SUFFIX);
		writer.newLine();
	}
}
