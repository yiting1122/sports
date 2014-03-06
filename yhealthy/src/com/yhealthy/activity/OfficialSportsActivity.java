package com.yhealthy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yhealthy.activity.R.string;
import com.yhealthy.model.SportsContents;
import com.yhealthy.net.CommnuicationFactory;
import com.yhealthy.net.HttpCommunicationFactory;
import com.yhealthy.net.NetCommnuicationAdapter;
import com.yhealthy.net.NetCommunication;
import com.yhealthy.utils.JsonUtils;
import com.yhealthy.utils.SharePersistentUtil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class OfficialSportsActivity extends CommonActivity {

	private ListView officiaListView;
	private List<Map<String, Object>> mData=new ArrayList<Map<String,Object>>();
	private List<SportsContents> sportsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sports);
		
		initListView();
	}
	private void initListView() {
		// TODO Auto-generated method stub
		
		//初始化地步menu的事件响应
		ImageViewOffical=(ImageView) this.findViewById(R.id.menu_offical_img);
		ImageViewPersonnal=(ImageView)this.findViewById(R.id.menu_personnal_img);
		ImageViewRecommand=(ImageView)this.findViewById(R.id.menu_recommend_img);
		imageViewMore=(ImageView)this.findViewById(R.id.menu_more_img);
		ImageViewOffical.setOnTouchListener(viewIndexListener);
		ImageViewPersonnal.setOnTouchListener(viewPersonnalListener);
		ImageViewRecommand.setOnTouchListener(viewRecommandListener);
		imageViewMore.setOnTouchListener(viewMoreListener);
		
		ImageViewOffical.setImageResource(R.drawable.menu_home_pressed);
		//加载数据
		officiaListView=(ListView)this.findViewById(R.id.listview);
		(new OfficalSportsTask()).execute();
	}
	
	private final class OfficalSportsTask extends AsyncTask<Void,Integer,List<SportsContents>>
	{
		private final Context context=getApplicationContext();
		@Override
		protected List<SportsContents> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			Map<String, String> map = new HashMap<String, String>();
//			map.put("type", MonitorType.OTHER.values() + "");
//			map.put(, value)
			SharePersistentUtil util=SharePersistentUtil.getInstance();
			String cityId=util.get(context, "cityId");
			String latitude=util.get(context, "latitude");
			String longitude=util.get(context, "longitude");
			byte[] bytes = null;
			List<SportsContents> sportsContents = null;
			try {
				bytes = netCommunication.sendRequest(
						getString(R.string.load_sportscontent_action), map,
						"utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bytes != null && bytes.length > 0) {
				String ret = JsonUtils.parserJson(bytes,
						NetCommnuicationAdapter.SUCCESS) + "";
				if (NetCommnuicationAdapter.TRUE.equals(ret)) {
					sportsContents = JsonUtils.parserJsonObject(bytes,
							"results", SportsContents.class);
				}
			}
			return sportsContents;
		}

		@Override
		protected void onPostExecute(List<SportsContents> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null&&result.size()>0&&(!result.isEmpty()))
			{
				sportsList=result;
				if(mData!=null)
				{
				    mData.clear();
				}
			   bindListView(result);
			}
		}
				
		
	}
	
	
	private void bindListView(List<SportsContents> result)
	{
		
		for(SportsContents sport:result)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", sport.getContentName());
			String number="5";
			map.put("number", number);
			map.put("address", sport.getAddress());
			mData.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, mData,
				R.layout.common_list_item, new String[] { "name","number","address"},
				new int[] { R.id.sports_name,R.id.sports_number,R.id.sports_address});
		 officiaListView.setAdapter(simpleAdapter);
		 officiaListView.setOnItemClickListener(new ItemClickListener());
	}
	
	
	private final class ItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			SportsContents sportsContent=(SportsContents) sportsList.get(position);
		    Intent intent=new Intent();
		    intent.setClass(getApplicationContext(), SportsDetailActivity.class);
		    Bundle bundle=new Bundle();
		    bundle.putSerializable("sportsInfo", sportsContent);
		    intent.putExtras(bundle);
		    startActivity(intent);
		}
		
	}
	
	
	
//	
//	 public final class ViewHolder {
//			public ImageView img;
//			public TextView title;
//		}
//	    
//	    public class MyAdapter extends BaseAdapter {
//
//			private LayoutInflater mInflater;
//
//			public MyAdapter(Context context) {
//				this.mInflater = LayoutInflater.from(context);
//			}
//
//			@Override
//			public int getCount() {
//				// TODO Auto-generated method stub
//				return mData.size();
//			}
//
//			@Override
//			public Object getItem(int arg0) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public long getItemId(int arg0) {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//
//				ViewHolder holder = null;
//				if (convertView == null) 
//				{
//					holder = new ViewHolder();
//
//					convertView = mInflater.inflate(R.layout.common_list_item, null);
//					convertView.setMinimumHeight(100);
//					holder.img = (ImageView) convertView.findViewById(R.id.category_icon);
//					holder.title = (TextView) convertView.findViewById(R.id.category_name);
//
//					convertView.setTag(holder);
//
//				} 
//				else 
//				{
//
//					holder = (ViewHolder) convertView.getTag();
//				}
////				holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
//				holder.title.setText((String) mData.get(position).get("title"));
//				
//				return convertView;
//			}
//
//		}

}
