package com.yhealthy.Location;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.yhealthy.activity.ApplicationConfig;
import com.yhealthy.service.WeatherService;
import com.yhealthy.utils.SharePersistentUtil;

public class MyLocation {
	private static Double latitude;
	private static Double longitude;
	private static String province;
	private static String city;
	private  LocationData locData = null;
	public static LocationClient mLocClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	private Context mContext;
	private Application application;

	public MyLocation(Context mContext,Application application) {
		super();
		this.mContext = mContext;
		this.application=application;
	}



	public void getLocation() {
		if (mLocClient == null) {
			mLocClient = new LocationClient(mContext);
			mLocClient.registerLocationListener(myListener);

			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(5000);
			mLocClient.setLocOption(option);
			mLocClient.start();
		}
		locData = new LocationData();

	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
//			Toast.makeText(mContext, locData.latitude + "x", Toast.LENGTH_LONG)
//					.show();
			
//			Toast.makeText(mContext, locData.longitude + "y", Toast.LENGTH_LONG)
//					.show();
			ApplicationConfig app = (ApplicationConfig) application;
			MKSearch mSearch = new MKSearch();
			mSearch.init(app.mBMapManager, new MySearchListener());
			mSearch.reverseGeocode(new GeoPoint((int)(locData.latitude*1e6),(int)(locData.longitude*1e6)));
			latitude=locData.latitude;
			longitude=locData.longitude;
			SharePersistentUtil util=SharePersistentUtil.getInstance();
			util.put(mContext, "latitude",latitude.toString());
			util.put(mContext, "longitude",longitude.toString());
			mLocClient.unRegisterLocationListener(myListener);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			// 返回地址信息搜索结果
			if (error != 0) {  
		        String str = String.format("错误号：%d", error);  
		        Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();  
		        return;  
		    }   
		    if (res.type == MKAddrInfo.MK_GEOCODE) {  
		        //地理编码：通过地址检索坐标点  
		        String strInfo = String.format("纬度：%f 经度：%f", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);  
		        Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();  
		    }  
		    if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {  
		        //反地理编码：通过坐标点检索详细地址及周边poi  
		        String strInfo = res.strAddr;  
		        
		        province=strInfo.substring(0,strInfo.lastIndexOf("省"));
		        city=strInfo.substring(strInfo.lastIndexOf("省")+1,strInfo.lastIndexOf("市"));
		        Toast.makeText(mContext, province+" "+city, Toast.LENGTH_LONG).show();
		        if (mLocClient != null)
					mLocClient.stop();
		        //获取天气
		        SharePersistentUtil util=SharePersistentUtil.getInstance();
		        util.put(mContext, "city", city);
				 WeatherService service=new WeatherService(mContext);
				 service.getWeather(city);
				 
		    } 
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// 返回poi搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// 返回联想词信息搜索结果
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}
}
