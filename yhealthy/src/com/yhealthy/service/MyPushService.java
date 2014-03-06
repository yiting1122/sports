package com.yhealthy.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tencent.weibo.sdk.android.api.util.Util;
import com.yhealthy.activity.R;
import com.yhealthy.activity.SystemActivity;
import com.yhealthy.login.LoginConstants;
import com.yhealthy.net.CommnuicationFactory;
import com.yhealthy.net.HttpCommunicationFactory;
import com.yhealthy.net.NetCommunication;
import com.yhealthy.utils.SharePersistentUtil;

public class MyPushService {

	private String userName;
	private String password;
	private String pushId;
	private int loginClass;
	
	private String openId;
	private String expires_In;
	private String access_Token;
	

	private Context mcontext;
	public MyPushService(Context mcontext) {
		this.mcontext=mcontext;
		SharePersistentUtil util=SharePersistentUtil.getInstance();
		this.pushId=util.get(mcontext, "pushId");
		this.loginClass = util.getInt(mcontext, "loginClass");
		if(loginClass==LoginConstants.SYSTEM_LOGIN)
		{
			this.userName=util.get(mcontext,"username");
			this.password=util.get(mcontext, "password");
		}
		if(loginClass==LoginConstants.QQ_LOGIN)
		{
			openId=Util.getSharePersistent(mcontext, "OPEN_ID");
			expires_In=Util.getSharePersistent(mcontext, "EXPIRES_IN");
			access_Token=Util.getSharePersistent(mcontext, "ACCESS_TOKEN");
		}
		if(loginClass==LoginConstants.QQWEIBO_LOGIN)
		{
			openId=Util.getSharePersistent(mcontext, "OPEN_ID");
			expires_In=Util.getSharePersistent(mcontext, "EXPIRES_IN");
			access_Token=Util.getSharePersistent(mcontext, "ACCESS_TOKEN");
		}
		if(loginClass==LoginConstants.SINA_LOGIN)
		{
			openId=util.get(mcontext, "open_Id");
			expires_In=util.get(mcontext, "expires_In");
			access_Token=util.get(mcontext, "expires_In");
		}
	}
	
	public void execute()
	{
		new UpdatePushId().execute("");
	}
	
	public void executeByOpen()
	{
		new UpdatePushIdByOpen().execute("");
	}
	
	
	private final class UpdatePushId extends AsyncTask<String, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userName);
			map.put("password", password);
			map.put("pushId", pushId);
			map.put("loginclass", String.valueOf(loginClass));
			try {
				String url=mcontext.getString(R.string.setPushAction_action);
				Boolean tag=netCommunication.validateRequest(url, map, "UTF-8");            
				return tag;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result==false)
			{
				Toast.makeText(mcontext, "绑定openId失败", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
			
	}
	
	
	
	
	private final class UpdatePushIdByOpen extends AsyncTask<String, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			Map<String, String> map = new HashMap<String, String>();
			map.put("openId", openId);
			map.put("pushId", pushId);
			map.put("loginclass", String.valueOf(loginClass));
			try {
				String url=mcontext.getString(R.string.setPushActionByOpen_action);
				Boolean tag=netCommunication.validateRequest(url, map, "UTF-8");            
				return tag;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result==false)
			{
				Toast.makeText(mcontext, "绑定openId失败", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
			
	}
	
}
