package com.yhealthy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.android.pushservice.PushManager;
import com.baidu.mapapi.BMapManager;
import com.yhealthy.Location.MyLocation;
import com.yhealthy.login.LoginConstants;
import com.yhealthy.push.MyPush;
import com.yhealthy.service.MyPushService;
import com.yhealthy.service.WeatherService;
import com.yhealthy.utils.AppDatabaseUtils;
import com.yhealthy.utils.DatabaseHelper;
import com.yhealthy.utils.MSG;
import com.yhealthy.utils.SharePersistentUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SystemActivity extends Activity {

	private GridView officalSportsGridView;
	private GridView personnalSportsGridView;
	private ListView othersListView;
	private List<Map<String, Object>> mData;
	private TextView weatherTextView;
	private WeatherBroadcastReceiver receiver;
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
//			super.handleMessage(msg);
			switch(msg.what){
			case MSG.UPDATE_WEATHER:
				weatherTextView.setText(msg.getData().getString("weather"));
				weatherTextView.setVisibility(View.VISIBLE);
//				unregisterReceiver(receiver); //结束时才注销广播 这里不能注销 否则不知道 广播是否已经注销
			}
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		createDatabase();
		initViews();
		registerBroadCastReceive();
	}
	
	private void registerBroadCastReceive() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.WeatherService");
		receiver=new WeatherBroadcastReceiver();
		this.registerReceiver(receiver, filter);
		 MyLocation myLocation=new MyLocation(this,this.getApplication());
		 myLocation.getLocation();
		 

	}

	private void createDatabase()
	{
		if (!AppDatabaseUtils.isExistDatabase(this)) {
			DatabaseHelper databaseHelper = new DatabaseHelper(this);
			databaseHelper.getWritableDatabase();
			databaseHelper.close();
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		officalSportsGridView = (GridView) this
				.findViewById(R.id.gridView_officalSports);
		personnalSportsGridView = (GridView) this
				.findViewById(R.id.gridView_personnalSports);
		othersListView=(ListView)this.findViewById(R.id.listview_others);
		weatherTextView=(TextView)this.findViewById(R.id.weather_bar);
		initOfficalSportsGrid();
		initPersonnalSportsGrid();
		officalSportsGridView.setOnItemClickListener(new GridViewItemClickListerner());
		personnalSportsGridView.setOnItemClickListener(new GridViewItemClickListerner());
		
		 mData = CategoryData.getData();
		 ListAdapter adapter = new MyAdapter(this);
		 othersListView.setAdapter(adapter);
		 
		 //map地图
			ApplicationConfig app = (ApplicationConfig) this.getApplication();
			if (app.mBMapManager == null) {
				app.mBMapManager = new BMapManager(this);
				app.mBMapManager.init(ApplicationConfig.strKey,
						new ApplicationConfig.MyGeneralListener());
			}
			//获取天气	
			 SharePersistentUtil util=SharePersistentUtil.getInstance();
			 String city=util.get(this,"city");
			 if(!"".equals(city))
			 {
				 WeatherService service=new WeatherService(this);
				 service.getWeather(city);
			 }
			 
			 //初始化推送
			 initPush();
			 //更新user pushId
			 

	}


	private void initPush() {
		// TODO Auto-generated method stub
		MyPush myPush=MyPush.getInstancce(this);
		myPush.start();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}

	@Override
	public void onResume() {
		super.onResume();

	}


	@Override
	protected void onNewIntent(Intent intent) {
		// 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
		setIntent(intent);
		MyPush myPush=MyPush.getInstancce(this);
		myPush.handleIntent(intent);
	}

	@Override
	public void onStop() {
		super.onStop();
		PushManager.activityStoped(this);
	}
	
    @Override
    protected void onDestroy() {
        ApplicationConfig app = (ApplicationConfig)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        if(receiver!=null)
        {
        	unregisterReceiver(receiver);
        }
        if(MyLocation.mLocClient!=null)
        {
        	MyLocation.mLocClient.stop();
        }
        super.onDestroy();
    }
    
    
    
    
	private void initOfficalSportsGrid() {
		Integer[] imageIds = { R.drawable.a1, R.drawable.a2, R.drawable.a3 };
		String[] values = { "官方活动", "私人活动", "摇一摇", };
		List<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < values.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", imageIds[i]);
			map.put("ItemText", values[i]);
			map.put("id", i);
			lstImageItem.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, lstImageItem,
				R.layout.systemclassitem, new String[] { "ItemImage",
						"ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		officalSportsGridView.setAdapter(simpleAdapter);
		
		

	}

	private void initPersonnalSportsGrid() {
		Integer[] imageIds = { R.drawable.a4, R.drawable.a5, R.drawable.a6 };
		String[] values = { "开始运动", "运动历史", "运动等级", };
		List<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < values.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", imageIds[i]);
			map.put("ItemText", values[i]);
			map.put("id", i);
			lstImageItem.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, lstImageItem,
				R.layout.systemclassitem, new String[] { "ItemImage",
						"ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		personnalSportsGridView.setAdapter(simpleAdapter);

	}

	private Class<?> mActivitys[] = { OfficialSportsActivity.class, SystemActivity.class };

	private final class GridViewItemClickListerner implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			HashMap<String, Object> item = (HashMap<String, Object>) parent
					.getItemAtPosition(position);
			// Toast.makeText(getApplicationContext(), item.get("id")+"",
			// 1).show();

			Integer activityId = Integer.parseInt(item.get("id").toString());
			Intent intent = new Intent(getApplicationContext(),
					mActivitys[activityId]);
			startActivity(intent);
		}
	}
	
	
	
	 private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
	        {
//	            Intent intent = new Intent();
//	            intent.setClass(CategoryActivity.this, ResultActivity.class);
//	            startActivity(intent);
	        }
	    };
	    
	    
	    public final class ViewHolder {
			public ImageView img;
			public TextView title;
		}
	    
	    public class MyAdapter extends BaseAdapter {

			private LayoutInflater mInflater;

			public MyAdapter(Context context) {
				this.mInflater = LayoutInflater.from(context);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mData.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				ViewHolder holder = null;
				if (convertView == null) 
				{
					holder = new ViewHolder();

					convertView = mInflater.inflate(R.layout.main_list_item, null);
					convertView.setMinimumHeight(100);
					holder.img = (ImageView) convertView.findViewById(R.id.category_icon);
					holder.title = (TextView) convertView.findViewById(R.id.category_name);

					convertView.setTag(holder);

				} 
				else 
				{

					holder = (ViewHolder) convertView.getTag();
				}
				holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
				holder.title.setText((String) mData.get(position).get("title"));
				
				return convertView;
			}

		}
	    
	 
	    
	    
	    private final class WeatherBroadcastReceiver extends BroadcastReceiver
	    {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if(intent.getAction().equals("android.intent.action.WeatherService"))
				{
				Message message=mHandler.obtainMessage();
				message.what=MSG.UPDATE_WEATHER;
				Bundle bundle=intent.getExtras();
				message.setData(bundle);
				message.sendToTarget();
				}
			}
	    	
	    }
	    
	    
}
	



