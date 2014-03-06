package com.yhealthy.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetState {
	private Context mContext;
	
	
	public NetState(Context mContext) {
		this.mContext = mContext;
	}


	public boolean checkNetWorkStatus() {
		boolean netWorkStatus = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (networkInfo != null) {
			netWorkStatus = networkInfo.isAvailable();
		}
		if (!netWorkStatus) {
			Builder builder = new AlertDialog.Builder(
					mContext).setTitle(
					"网络没开启").setMessage(
					"是否打开网络");
		   builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			Intent intent=new Intent();
                if(android.os.Build.VERSION.SDK_INT>10){
                	intent.setAction(android.provider.Settings.ACTION_SETTINGS);
                }
                else {
				   intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);	
				}
                
			    mContext.startActivity(intent);
			}
		}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}).show();	
		   
		}
		return netWorkStatus;
	}
}
