package com.yhealthy.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yhealthy.dao.CityDao;
import com.yhealthy.dao.ProvinceDao;
import com.yhealthy.daoImp.CityDaoImp;
import com.yhealthy.daoImp.ProvinceDaoImp;
import com.yhealthy.exception.ApplicationException;
import com.yhealthy.model.City;
import com.yhealthy.model.Province;
import com.yhealthy.utils.SharePersistentUtil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class WeatherService {
	private Context mContext;
	public WeatherService(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public void getWeather(String cityName)
	{
		String weatherUrl="http://www.weather.com.cn/data/cityinfo/101";  //拼接获取城市
		String urlEnd="01.html";
		CityDao dao=new CityDaoImp(mContext);
		Map< String, Object> map=new HashMap<String, Object>();
		map.put("name", cityName);
		List<City>results=dao.selectByField(map, null);
		String queryCityId=results.get(0).getId();
		SharePersistentUtil util=SharePersistentUtil.getInstance();
		util.put(mContext, "cityId", queryCityId);
		String queryUrl=weatherUrl+queryCityId+urlEnd;
		(new weatherTask()).execute(queryUrl);
	}

	public void getCity()
	{	
		(new ProvinceTask()).execute("http://m.weather.com.cn/data5/city.xml");
	}
	
	
	private final class weatherTask extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String url = params[0];
			String result=download(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
			saveWeather(result);
			super.onPostExecute(result);
		}

		private void saveWeather(String result) {
			// TODO Auto-generated method stub
			try {
				JSONObject object=new JSONObject(result);
				JSONObject ret= object.getJSONObject("weatherinfo");
				String temperature1=ret.getString("temp1");
				String temperature2=ret.getString("temp2");
				String weather=ret.getString("weather");
				String highTemperature="";
				String lowTemperature="";
				if(temperature1.compareTo(temperature2)>0)
				{
					highTemperature=temperature1;
					lowTemperature=temperature2;
				}
				else {
					highTemperature=temperature2;
					lowTemperature=temperature1;
				}
				
				
				Intent intent = new Intent();
				intent.setAction("android.intent.action.WeatherService");
				Bundle bundle = new Bundle();
				bundle.putString("weather", "今日最低温度："+lowTemperature+",今日最高温度为："+highTemperature+",天气:"+weather);
				intent.putExtras(bundle);
				mContext.sendBroadcast(intent);

				
//				Toast.makeText(mContext, highTemperature+lowTemperature+weather, Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	private final class ProvinceTask extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String url = params[0];
			String result=download(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
			saveProvince(result);
			super.onPostExecute(result);
		}
		
		
	}
	
	
	private final class CityTask extends AsyncTask<String, Integer, String>
	{

		private String provinceId;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String url = params[0];
			provinceId=params[1];
			String result=download(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
			saveCity(result,provinceId);
			super.onPostExecute(result);
		}
		
		
	}
	
	private void saveProvince(String resource)
	{
		String[] array=resource.split(",");
		String cityUrl="http://m.weather.com.cn/data5/city";  //拼接获取城市
		String urlEnd=".xml";
		for(String s:array)
		{
			String[] temp=s.split("\\|");  //使用转义符号，凡是有符号的都要转义
			String provinceId;
			if(temp.length>0)
			{
				provinceId=temp[0];
				String provinceName=temp[1];
				Province province=new Province();
				province.setId(provinceId);
				province.setName(provinceName);
				ProvinceDao provinceDao=new ProvinceDaoImp(mContext);
				try {
					provinceDao.save(province);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	Toast.makeText(mContext, provinceId+"   "+provinceName, Toast.LENGTH_SHORT).show();
				
				if(provinceId.length()>0)
				{
					String query=cityUrl+provinceId+urlEnd;
					String[] queryArray = new String[]{"",""};
					queryArray[0]=query;
					queryArray[1]=provinceId;
					(new CityTask()).execute(queryArray);
				}
			}
		}
	}
	
	private void saveCity(String resource,String provinceId)
	{
		String[] array=resource.split(",");
		List<City> citysList =new ArrayList<City>();
		for(String s:array)
		{
			if(citysList!=null)
			{
			    citysList.clear();
			}
			String[] temp=s.split("\\|");  //使用转义符号，凡是有符号的都要转义
			if(temp.length>0)
			{
				String cityId=temp[0];
				String cityName=temp[1];
			
				City city=new City();
				city.setId(cityId);
				city.setName(cityName);
				city.setIshot(0);
				city.setProvinceId(provinceId);
				citysList.add(city);
			}
			try {
				CityDao dao=new CityDaoImp(mContext);
				dao.save(citysList);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String download(String urlStr){    
        StringBuffer sb = new StringBuffer();    
        String line = null;    
        BufferedReader buffer = null;    
        try {    
            URL url = new URL(urlStr);    
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();    
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));    
            while( (line = buffer.readLine()) != null){    
                sb.append(line);    
            }    
             
        }     
        catch (Exception e) {    
            e.printStackTrace();    
        }    
        finally{    
            try {    
                buffer.close();
                
            } catch (IOException e) {    
                e.printStackTrace();    
            }  
        }    
        return sb.toString();    
    }  
	

}
