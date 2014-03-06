package com.yhealthy.activity;

import java.util.HashMap;
import java.util.Map;



import com.yhealthy.net.CommnuicationFactory;
import com.yhealthy.net.HttpCommunicationFactory;
import com.yhealthy.net.NetCommunication;
import com.yhealthy.utils.NetState;
import com.yhealthy.utils.SharePersistentUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText usernameEditText;
	private EditText passwordEditText;
	private EditText repasswordEditText;
	private TextView registerTextView;
	private ProgressDialog progressDialog=null;
	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		initWindow();
	}

	private void initWindow() {
		// TODO Auto-generated method stub
		usernameEditText = (EditText) this.findViewById(R.id.register_name);
		passwordEditText = (EditText) this.findViewById(R.id.register_password);
		repasswordEditText = (EditText) this
				.findViewById(R.id.register_repassword);
        registerTextView=(TextView)this.findViewById(R.id.register);
		registerTextView.setOnClickListener(new RegisterClickListener());

	}

	private final class RegisterClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			username=usernameEditText.getText().toString();
			password=passwordEditText.getText().toString();
			String repassword=repasswordEditText.getText().toString();
			if ((!"".equals(username))&& (!"".equals(password))) {
                  if(password.equals(repassword))
                  {
                	  NetState state=new NetState(getApplicationContext());
                	  if(state.checkNetWorkStatus()==true)
                	  {
//                		  progressDialog=ProgressDialog.show(getApplicationContext(), "登录", "登录中",false,true);
                	      new RegisterTask().execute();
                	  }
                  }
			}
		}

	}
	
	
	private final class RegisterTask extends AsyncTask<Void, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", username);
			map.put("password", password);
			try {
				String url=getString(R.string.userAndroidRegister_action);
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
			if(result==true)
			{
				if(progressDialog!=null)
				{
				    progressDialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
				SharePersistentUtil util=SharePersistentUtil.getInstance();
				util.put(getApplicationContext(), "username",username);
				util.put(getApplicationContext(), "password",password);
				Intent intent=new Intent(getApplicationContext(),SystemActivity.class);
				startActivity(intent);
				
			}
			else {
				Toast.makeText(getApplicationContext(), "注册失败，用户名不唯一，请重新填写", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
		
	}
	
	
}
