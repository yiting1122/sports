package com.yhealthy.push;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.yhealthy.utils.SharePersistentUtil;
import com.yhealthy.utils.Utils;


public class MyPush {
	private Context mContext;
	private MyPush(Context mContext)
	{
		this.mContext=mContext;
	}
	public static final MyPush getInstancce(Context mcontext)
	{
		return new MyPush(mcontext);
	}
	
	public void start()
	{
		PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_API_KEY, 
				Utils.getMetaValue(mContext, "api_key"));
	}
	public void stop()
	{
		PushManager.stopWork(mContext);
	}
	public void resume()
	{
		PushManager.resumeWork(mContext);
	}
	
	public boolean isEnabled()
	{
		return PushManager.isPushEnabled(mContext);
	}
	public void setTags(List<String> tags)
	{
		PushManager.setTags(mContext, tags);
	}
	public void deleteTags(List<String> tags)
	{
		PushManager.delTags(mContext, tags);
	}
	
	public void openDebugMode()
	{
		PushSettings.enableDebugMode(mContext, true);
	}
	public void closeDebugMode()
	{
		PushSettings.enableDebugMode(mContext, false);
	}
	
	
	
	public void handleIntent(Intent intent) {
		String action = intent.getAction();

		if (Utils.ACTION_RESPONSE.equals(action)) {

			String method = intent.getStringExtra(Utils.RESPONSE_METHOD);

			if (PushConstants.METHOD_BIND.equals(method)) {
				String toastStr = "";
				int errorCode = intent.getIntExtra(Utils.RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent
							.getStringExtra(Utils.RESPONSE_CONTENT);
					String appid = "";
					String channelid = "";
					String userid = "";

					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent
								.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");
					} catch (JSONException e) {
						Log.e(Utils.TAG, "Parse bind json infos error: " + e);
					}

//					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
//					Editor editor = sp.edit();
//					editor.putString("appid", appid);
//					editor.putString("channel_id", channelid);
//					editor.putString("user_id", userid);
//					editor.commit();				
//					showChannelIds();

					SharePersistentUtil util=SharePersistentUtil.getInstance();
					util.put(mContext,"appid", appid);
					util.put(mContext,"channelId", channelid);
					util.put(mContext,"pushId", userid);
					toastStr = "Bind Success";
				} else {
					toastStr = "Bind Fail, Error Code: " + errorCode;
					if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}

				Toast.makeText(mContext, toastStr, Toast.LENGTH_LONG).show();
			}
		}  else if (Utils.ACTION_MESSAGE.equals(action)) {
			String message = intent.getStringExtra(Utils.EXTRA_MESSAGE);
			String summary = "Receive message from server:\n\t";
			Log.e(Utils.TAG, summary + message);
			JSONObject contentJson = null;
			String contentStr = message;
			try {
				contentJson = new JSONObject(message);
				contentStr = contentJson.toString(4);
			} catch (JSONException e) {
				Log.d(Utils.TAG, "Parse message json exception.");
			}
			summary += contentStr;
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(summary);
			builder.setCancelable(true);
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Log.i(Utils.TAG, "Activity normally start!");
		}
	}

//	public void showChannelIds() {
//		String appId = null;
//		String channelId = null;
//		String clientId = null;
//
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
//		appId = sp.getString("appid", "");
//		channelId = sp.getString("channel_id", "");
//		clientId = sp.getString("user_id", "");
//		String content = "\tApp ID: " + appId + "\n\tChannel ID: " + channelId
//				+ "\n\tUser ID: " + clientId + "\n\t";
//		Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
//	}
}
