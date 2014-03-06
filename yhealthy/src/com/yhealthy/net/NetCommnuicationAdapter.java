package com.yhealthy.net;

import java.io.InputStream;
import java.util.Map;

import com.yhealthy.utils.FileUtils;
import com.yhealthy.utils.JsonUtils;


public abstract class NetCommnuicationAdapter implements NetCommunication {
	public final static String SUCCESS = "success";
	public final static String TRUE = "true";

	@Override
	public byte[] sendRequest(String url) throws Exception {
		// TODO Auto-generated method stub
		return sendRequest(url, null, null);
	}

	@Override
	public byte[] sendRequest(String url, Map<String, String> map,
			String encoding) throws Exception {
		// TODO Auto-generated method stub
		InputStream in = downRequest(url, map, encoding);
		if (in != null) {
			return FileUtils.readInputStrem(in);
		}
		return null;
	}

	@Override
	public boolean validateRequest(String url) throws Exception {
		// TODO Auto-generated method stub
		return validateRequest(url, null, null);
	}

	@Override
	public InputStream downRequest(String url) throws Exception {
		// TODO Auto-generated method stub
		return downRequest(url, null, null);
	}

	@Override
	public boolean validateRequest(String url, Map<String, String> map,
			String encoding) throws Exception {
		// TODO Auto-generated method stub
		InputStream in = downRequest(url, map, encoding);
		byte[] data = FileUtils.readInputStrem(in);
		String ret = JsonUtils.parserJson(data, SUCCESS) + "";
		if (TRUE.equals(ret)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean uploadFile(String url, Map<String, String> map, FormFile file)
			throws Exception {
		return uploadFile(url, map, new FormFile[] { file });
	}

}
