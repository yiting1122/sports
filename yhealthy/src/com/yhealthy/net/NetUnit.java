package com.yhealthy.net;

import java.io.File;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import android.R.string;
import android.test.AndroidTestCase;
import android.util.Log;

public class NetUnit extends AndroidTestCase {
	private String tag = "net.NetUnit";

	public void test1() throws Exception {

		CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
		NetCommunication netCommunication = httpCommunicationFactory.factory();
		String url = "http://192.168.1.164:8080/VATTI/test.action";
		// String url="http://www.baidu.com";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "����������rb");
		map.put("password", "1122");
		byte[] data = netCommunication.sendRequest(url, map, "UTF-8");
		if (data != null && data.length > 0) {

			// String string =new Strin
			String content = new String(data, "GBK");
			// String temp=new String(content.getBytes("ISO8859-1"),"GBK");
			// /temp+="������555555";
			// value.getBytes(����,��UTF-8��)
			Log.i(tag, content);
		}
	}

	public void test2() throws Exception {
		String fileName = "/data/data/com.example.unittest/files/����.txt";
		File file = new File(fileName);
		if (file != null && file.exists()) {
			FormFile formFile = new FormFile(file, "file", "text/html");
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory();
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			String url = "http://192.168.1.164:8080/VATTI/upload.action";
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("fileName", file.getName());
			boolean flag = netCommunication.uploadFile(url, parms, formFile);
			Log.i(tag, flag + "");

		}
	}

	public void test3() throws Exception {

		CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory(
				);
		NetCommunication netCommunication = httpCommunicationFactory.factory();
		String url = "http://192.168.1.164:8080/VATTI/test.action";
		// String url="http://www.baidu.com";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "����������rb");
		map.put("password", "1122");
		byte[] data = netCommunication.sendRequest(url, map, "UTF-8");
		if (data != null && data.length > 0) {

			// String string =new Strin
			String content = new String(data, "GBK");
			// String temp=new String(content.getBytes("ISO8859-1"),"GBK");
			// /temp+="������555555";
			// value.getBytes(����,��UTF-8��)
			Log.i(tag, content);
		}
	}

	public void test4() throws Exception {
		String fileName = "/data/data/com.example.unittest/files/����.txt";
		File file = new File(fileName);
		if (file != null && file.exists()) {
			FormFile formFile = new FormFile(file, "file", "text/html");
			CommnuicationFactory httpCommunicationFactory = new HttpCommunicationFactory(
					);
			NetCommunication netCommunication = httpCommunicationFactory
					.factory();
			String url = "http://192.168.1.164:8080/VATTI/upload.action";
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("fileName", file.getName());
			boolean flag = netCommunication.uploadFile(url, parms, formFile);
			Log.i(tag, flag + "");

		}
	}

	public void test5() throws Exception {
		// String temp="{\""
	}

}
