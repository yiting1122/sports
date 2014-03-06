package com.yhealthy.service;

import java.util.HashMap;
import java.util.Map;

import com.baidu.android.common.logging.Log;
import com.yhealthy.activity.LoginActivity;
import com.yhealthy.activity.R;
import com.yhealthy.activity.SystemActivity;
import com.yhealthy.login.LoginConstants;
import com.yhealthy.net.CommnuicationFactory;
import com.yhealthy.net.HttpCommunicationFactory;
import com.yhealthy.net.NetCommunication;
import com.yhealthy.utils.SharePersistentUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;

import android.widget.Toast;


public class LoginService {
	private Context context;
	private Activity activity;
	private String userName;
	private String password;
//    private ProgressDialog progressDialog=null;
	public LoginService(Context context,Activity activity) {
		super();
		this.context = context;
		this.activity=activity;
	}

	private LoginTask task;
	private boolean success = false;

	public void Login(String username, String password) throws Exception {
//		progressDialog=ProgressDialog.show(context, "登录", "系统登录中",false,true);      
		this.userName=username;
		this.password=password;
		task = new LoginTask();
		task.execute(username, password);
		
	}

	private void cancle() {
		task.cancel(true);
	}

	private final class LoginTask extends AsyncTask<String, Integer, Boolean>

	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			
			if (result) {
				success = true;
				SharePersistentUtil util=SharePersistentUtil.getInstance();
				util.put(context, "username", userName);
				util.put(context, "password", password);
				util.put(context, "loginClass", LoginConstants.SYSTEM_LOGIN);
				Log.i("Write user and password", "write user password success");
				Intent intent = new Intent(context, SystemActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intent);
				activity.finish();
			} else {
				success = false;
				Toast.makeText(context, "登录失败，用户名或者密码错误", 1).show();				
			}		
			super.onPostExecute(result);
//			 if(progressDialog!=null){
//			    	progressDialog.dismiss();
//			    }
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			String url = context.getString(R.string.userAndroidLogin_action);
			String username = params[0];
			String password = params[1];
		
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", username);
			map.put("password", password);

			try {
				Boolean tag=netCommunication.validateRequest(url, map, "UTF-8");          
				return tag;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

	}

	
	
}
