package com.yhealthy.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;

import java.net.URL;

import java.util.Map;

import com.yhealthy.utils.UrlCodeUtils;

import android.util.Log;

public class HttpCommunicationFactory implements CommnuicationFactory {
	private NetCommunication netCommunication=null;
	private final String TAG = "net.HttpCommunicationFactory";

	public  HttpCommunicationFactory() {
		if (netCommunication==null) {
			netCommunication = new HttpCommnuication();
		}
		
	}

	@Override
	public synchronized NetCommunication factory() {
		// TODO Auto-generated method stub
	
		return netCommunication;
	}

	private class HttpCommnuication extends NetCommnuicationAdapter {
		private final String ENCODING = "UTF-8";
		private final String BLANK = "_blank";
		

	

		@Override
		public InputStream downRequest(String url, Map<String, String> map,
				String encoding) throws Exception {
			// TODO Auto-generated method stub
			Log.i(TAG, "start to connect internet");
			if (url != null && url.trim().length() > 0) {
				if (encoding == null || encoding.trim().length() == 0) {
					encoding = this.ENCODING;
				}
				String parms = BLANK;
				if (map != null && !map.isEmpty()) {
					parms = "";
					Log.i(TAG, "add parms");
					for (Map.Entry<String, String> key : map.entrySet()) {
						parms += key.getKey() + "=";
						String temp = UrlCodeUtils.decode(key.getValue(),
								encoding);
						parms += temp + "&";
					}
				}
				parms = parms.substring(0, parms.length() - 1);
				byte[] entity = parms.getBytes();
				HttpURLConnection connection = (HttpURLConnection) new URL(url)
						.openConnection();

				connection.setRequestMethod("POST");
				connection.setConnectTimeout(5000);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.setRequestProperty("Charsert", encoding);
				connection.setRequestProperty("Content-Length",
						String.valueOf(entity.length));
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				DataOutputStream out = new DataOutputStream(
						connection.getOutputStream());
				out.write(entity);
				out.flush();
				out.close();
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					Log.i(TAG, "connect internet successfully");
					return connection.getInputStream();
				}
				// connection.disconnect();
			}
			Log.i(TAG, "fail to connect internet");
			return null;
		}

		@Override
		public boolean uploadFile(String url, Map<String, String> map,
				FormFile[] files) throws Exception {
			// TODO Auto-generated method stub
			
			final String BOUNDARY = "---------------------------7da2137580612"; // ��ݷָ���
			final String endline = "--" + BOUNDARY + "--\r\n";// ��ݽ����־

			int fileDataLength = 0;
			for (FormFile uploadFile : files) {// �õ��ļ�������ݵ��ܳ���
				StringBuilder fileExplain = new StringBuilder();
				fileExplain.append("--");
				fileExplain.append(BOUNDARY);
				fileExplain.append("\r\n");
				fileExplain.append("Content-Disposition: form-data;name=\""
						+ uploadFile.getParameterName() + "\";filename=\""
						+ uploadFile.getFilname() + "\"\r\n");
				fileExplain.append("Content-Type: "
						+ uploadFile.getContentType() + "\r\n\r\n");
				fileDataLength += fileExplain.length();
				if (uploadFile.getInStream() != null) {
					fileDataLength += uploadFile.getFile().length();
				} else {
					fileDataLength += uploadFile.getData().length;
				}
				fileDataLength += "\r\n".length();
			}
			StringBuilder textEntity = new StringBuilder();
			for (Map.Entry<String, String> entry : map.entrySet()) {// �����ı����Ͳ����ʵ�����
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
			// ���㴫����������ʵ������ܳ���
			int dataLength = textEntity.toString().getBytes().length
					+ fileDataLength + endline.getBytes().length;

			URL urlConnection = new URL(url);
			int port = urlConnection.getPort() == -1 ? 80 : urlConnection
					.getPort();
			Socket socket = new Socket(InetAddress.getByName(urlConnection
					.getHost()), port);
			OutputStream outStream = socket.getOutputStream();
			// �������HTTP����ͷ�ķ���
			String requestmethod = "POST " + urlConnection.getPath()
					+ " HTTP/1.1\r\n";
			outStream.write(requestmethod.getBytes());
			String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
			outStream.write(accept.getBytes());
			String language = "Accept-Language: zh-CN\r\n";
			outStream.write(language.getBytes());
			String contenttype = "Content-Type: multipart/form-data; boundary="
					+ BOUNDARY + "\r\n";
			outStream.write(contenttype.getBytes());
			String contentlength = "Content-Length: " + dataLength + "\r\n";
			outStream.write(contentlength.getBytes());
			String alive = "Connection: Keep-Alive\r\n";
			outStream.write(alive.getBytes());
			String host = "Host: " + urlConnection.getHost() + ":" + port
					+ "\r\n";
			outStream.write(host.getBytes());
			// д��HTTP����ͷ����HTTPЭ����дһ���س�����
			outStream.write("\r\n".getBytes());
			// �������ı����͵�ʵ����ݷ��ͳ���
			outStream.write(textEntity.toString().getBytes());
			// �������ļ����͵�ʵ����ݷ��ͳ���
			for (FormFile uploadFile : files) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append("--");
				fileEntity.append(BOUNDARY);
				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\""
						+ uploadFile.getParameterName() + "\";filename=\""
						+ uploadFile.getFilname() + "\"\r\n");
				fileEntity.append("Content-Type: "
						+ uploadFile.getContentType() + "\r\n\r\n");
				outStream.write(fileEntity.toString().getBytes());
				if (uploadFile.getInStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = uploadFile.getInStream()
							.read(buffer, 0, 1024)) != -1) {
						outStream.write(buffer, 0, len);
					}
					uploadFile.getInStream().close();
				} else {
					outStream.write(uploadFile.getData(), 0,
							uploadFile.getData().length);
				}
				outStream.write("\r\n".getBytes());
			}
			// ���淢����ݽ����־����ʾ����Ѿ�����
			outStream.write(endline.getBytes());

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			StringBuilder result = new StringBuilder();
			String temp = reader.readLine();
			while (temp != null) {
				result.append(temp);
				temp = reader.readLine();
			}
			outStream.flush();
			outStream.close();
			reader.close();
			socket.close();
			if (result.indexOf("200") >= 0 && result.indexOf("true") >= 0) {
				Log.i(TAG, "upload file successfully");
				return true;
			}
			Log.i(TAG, "fail to upload file");
			return false;
		}
	}

	
}
