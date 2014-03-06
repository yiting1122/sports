package com.yhealthy.activity;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.json.JSONObject;

import com.baidu.android.pushservice.PushManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

import com.yhealthy.login.AccessTokenKeeper;
import com.yhealthy.login.LoginConstants;
import com.yhealthy.push.MyPush;
import com.yhealthy.service.LoginService;
import com.yhealthy.service.WeatherService;
import com.yhealthy.utils.SharePersistentUtil;

import com.tencent.weibo.sdk.android.api.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText usernameEditText;
	private EditText passwordEditText;
	private CheckBox remeberCheckBox;
	private Button loginButton;
	private Button registerButton;
	private ImageButton sinaButton;
	private ImageButton qqweiboButton;
	private ImageButton qqButton;

	// sinaweibo paramater
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	// qq
	private Handler mHandler;
	private Dialog mProgressDialog;
	private Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_dialog);

		// WeatherService service=new WeatherService(this);
		// service.getCity();

		usernameEditText = (EditText) this.findViewById(R.id.username);
		passwordEditText = (EditText) this.findViewById(R.id.password);
		remeberCheckBox = (CheckBox) this.findViewById(R.id.login_cb_savepwd);
		loginButton = (Button) this.findViewById(R.id.login_btn);
		registerButton = (Button) this.findViewById(R.id.register_btn);
		sinaButton = (ImageButton) this.findViewById(R.id.sina_btn);

		// 各种认证按钮设置

		qqweiboButton = (ImageButton) this.findViewById(R.id.qqweibo_btn);
		qqButton = (ImageButton) this.findViewById(R.id.qq_btn);
		qqweiboButton.setOnClickListener(new QQweiboButtongClickListener());
		qqButton.setOnClickListener(new QQButtongClickListener());
		sinaButton.setOnClickListener(new SinaButtongClickListener());

		loginButton.setOnClickListener(new LoginButtonClickListener());
		registerButton.setOnClickListener(new RegisterButtonClickListener());

		mWeibo = Weibo.getInstance(LoginConstants.SINA_CONSUMER_KEY,
				LoginConstants.SINA_REDIRECT_URL);
		mTencent = Tencent.createInstance(LoginConstants.QQAPP_ID,
				getApplicationContext());
		
		initLoginText();
		
	}


	private void initLoginText() {
		// TODO Auto-generated method stub
		SharePersistentUtil util = SharePersistentUtil.getInstance();
		String username = util.get(LoginActivity.this, "username");
		String password = util.get(LoginActivity.this, "password");
		if (!"".equals(username)) {
			usernameEditText.setText(username + "");
			passwordEditText.setText(password + "");
		}

	}
	
	
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		AuthHelper.unregister(getApplicationContext());
		super.onDestroy();
	}


	private final class LoginButtonClickListener implements
			View.OnClickListener {

		@Override
		public void onClick(View v) {
			String username = usernameEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			if (!"".equals(username) && (!"".equals(password))) {
				LoginService service = new LoginService(getApplicationContext(),LoginActivity.this);
				try {
					service.Login(username, password);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
		}
	}

	private final class RegisterButtonClickListener implements
			View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
	}

	private final class QQweiboButtongClickListener implements
			View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			long appid = Long.valueOf(Util.getConfig().getProperty("APP_KEY"));
			String app_secket = Util.getConfig().getProperty("APP_KEY_SEC");
			auth(appid, app_secket);
//			if(!"".equals(Util.getSharePersistent(getApplicationContext(), "OPEN_ID")))
//			{
//				Intent intent = new Intent(getApplicationContext(),
//						SystemActivity.class);
//				startActivity(intent);
//				LoginActivity.this.finish();
//			}
		}

	}

	private final class QQButtongClickListener implements View.OnClickListener {
		final Context context = getApplicationContext();
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mProgressDialog = new ProgressDialog(LoginActivity.this);
			// mProgressDialog.show();
			if (!mTencent.isSessionValid()) {
				IUiListener listener = new BaseUiListener() {
					@Override
					protected void doComplete(JSONObject values) {
						try {
							
//						    if("success".equals(values.get("msg").toString()))
//						     {
						        String open_Id=values.getString("openid");
						        String expires_in=values.getString("expires_in");
						        String access_token=values.getString("access_token");
								SharePersistentUtil util=SharePersistentUtil.getInstance();
								util.put(getApplicationContext(), "loginClass", LoginConstants.QQ_LOGIN);
								util.put(context, "open_Id", open_Id);
								util.put(context, "expires_In", expires_in);
								util.put(context, "access_Token", access_token);
								util.put(context, "nickname", "");
								
								Intent intent = new Intent(getApplicationContext(),
										SystemActivity.class);
								startActivity(intent);
								LoginActivity.this.finish();
//						     }
						  } catch (Exception e) {
							// TODO: handle exception
						}
					}
				};
				mTencent.login(LoginActivity.this, LoginConstants.QQSCOPE,
						listener);
			} else {
				Toast.makeText(context,"已经通过验证" + mTencent.getOpenId(), Toast.LENGTH_LONG).show();
			}
		}

	}

	private final class SinaButtongClickListener implements
			View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mWeibo.authorize(LoginActivity.this, new AuthDialogListener());
//			accessToken = AccessTokenKeeper.readAccessToken(LoginActivity.this);

		
		}

	}

	/***
	 * 腾讯微博认证
	 * 
	 * @param appid
	 * @param app_secket
	 */
	private void auth(long appid, String app_secket) {
		final Context context = this.getApplicationContext();

		AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

			@Override
			public void onWeiBoNotInstalled() {
				Toast.makeText(LoginActivity.this, "onWeiBoNotInstalled", 1000)
						.show();
				Intent i = new Intent(LoginActivity.this, Authorize.class);
				startActivity(i);
			}

			@Override
			public void onWeiboVersionMisMatch() {
				Toast.makeText(LoginActivity.this, "onWeiboVersionMisMatch",
						1000).show();
				Intent i = new Intent(LoginActivity.this, Authorize.class);
				startActivity(i);
			}

			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(LoginActivity.this, "result : " + result, 1000)
						.show();
			}

			
			
			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				Toast.makeText(LoginActivity.this, "passed", 1000).show();
				//

				Util.saveSharePersistent(context, "ACCESS_TOKEN",
						token.accessToken);
				Util.saveSharePersistent(context, "EXPIRES_IN",
						String.valueOf(token.expiresIn));
				Util.saveSharePersistent(context, "OPEN_ID", token.openID);
				// Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
				Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
				// Util.saveSharePersistent(context, "NAME", name);
				// Util.saveSharePersistent(context, "NICK", name);
				Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig()
						.getProperty("APP_KEY"));
				Util.saveSharePersistent(context, "AUTHORIZETIME",
						String.valueOf(System.currentTimeMillis() / 1000l));
//				//写入登陆类型
//				SharePersistentUtil util=SharePersistentUtil.getInstance();
//				util.put(context, "loginClass", LoginConstants.QQWEIBO_LOGIN);
//				util.put(context, "open_Id", token.openID);
//				util.put(context, "expires_In", String.valueOf(token.expiresIn));
//				util.put(context, "access_Token", token.accessToken);
//				util.put(context, "nickname", name);
			}
		});

		AuthHelper.auth(this, "");
	}

	/***
	 * 新浪认证
	 * 
	 * @author yiting
	 * 
	 */
	class AuthDialogListener implements WeiboAuthListener {
		private final Context context=getApplicationContext();
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String userid = values.getString("uid");
			String name=values.getString("name");
			SharePersistentUtil util=SharePersistentUtil.getInstance();
			util.put(getApplicationContext(), "loginClass", LoginConstants.SINA_LOGIN);
			util.put(context, "open_Id", userid);
			util.put(context, "expires_In", expires_in);
			util.put(context, "access_Token", token);
			util.put(context, "nickname", name);

			LoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (LoginActivity.accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(LoginActivity.accessToken
								.getExpiresTime()));
				AccessTokenKeeper.keepAccessToken(LoginActivity.this,
						accessToken);
				Intent intent = new Intent(getApplicationContext(),
						SystemActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	/***
	 * QQ认证
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		final CharSequence[] serverList = { "正式环境" };

		SharedPreferences sp = getSharedPreferences(
				LoginConstants.QQSERVER_PREFS, 0);
		int serverType = sp.getInt(LoginConstants.QQSERVER_TYPE, 0);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Option").setCancelable(true)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismissDialog(LoginConstants.QQSELECT_SERVER_DIALOG);
					}
				});
		builder.setSingleChoiceItems(serverList, serverType,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							// mTencent.setEnvironment(AuthQQ.this, 0);
							dismissDialog(LoginConstants.QQSELECT_SERVER_DIALOG);
						}
						if (which == 1) {
							// mTencent.setEnvironment(AuthQQ.this, 1);
							dismissDialog(LoginConstants.QQSELECT_SERVER_DIALOG);
						}
					}
				});

		dialog = builder.create();
		return dialog;
	}

	private void showResult(final String base, final String msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				// mBaseMessageText.setText(base);
				// mMessageText.setText(msg);
			}
		});
	}

	private class BaseUiListener implements IUiListener {
		private final Context context=getApplicationContext();
		@Override
		public void onComplete(JSONObject response) {
			// mBaseMessageText.setText("onComplete:");
			// mMessageText.setText(response.toString());
			String valueString = response.toString();
			Toast.makeText(getApplicationContext(), valueString,
					Toast.LENGTH_LONG).show();
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {
	
		}

		@Override
		public void onError(UiError e) {
			showResult("onError:", "code:" + e.errorCode + ", msg:"
					+ e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			showResult("onCancel", "");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Log.v("sample","onActivityResult:" + requestCode);
		// must call mTencent.onActivityResult.
		if (mTencent !=null) {
			if (!mTencent.onActivityResult(requestCode, resultCode, data)) {
			}
		}
		
	}

}
