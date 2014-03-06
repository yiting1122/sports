package com.yhealthy.net;

import java.io.InputStream;
import java.util.Map;

public interface NetCommunication {

	public byte[] sendRequest(String url) throws Exception;

	public byte[] sendRequest(String url, Map<String, String> map,
			String encoding) throws Exception;

	public boolean validateRequest(String url) throws Exception;

	public boolean validateRequest(String url, Map<String, String> map,
			String encoding) throws Exception;

	public boolean uploadFile(String url, Map<String, String> map,
			FormFile[] files) throws Exception;

	public boolean uploadFile(String url, Map<String, String> map, FormFile file)
			throws Exception;

	public InputStream downRequest(String url) throws Exception;

	public InputStream downRequest(String url, Map<String, String> map,
			String encoding) throws Exception;
}
