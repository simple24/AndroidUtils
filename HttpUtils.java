package com.simplexu.day04.slidingviewpager;

import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HttpURLConnection请求工具类
 * 
 * @author simplexu
 * 
 */

public class HttpUtils {

	private static Handler handler = new Handler();

	private static ExecutorService executor = Executors.newFixedThreadPool(5); // 开辟线程池，体现性能优化

	/**
	 * 回调接口，更新UI
	 *
	 */
	public interface CallBack {
		void onRequestComplete(String result);
	}


	/**
	 * 异步的Get请求
	 * 
	 * @param urlStr
	 * 				发送请求的 URL
	 * @param callBack
	 * 				回调函数
	 */
	public static void doGetAsyn(final String urlStr, final CallBack callBack) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				final String result = doGet(urlStr);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (callBack != null) {
							callBack.onRequestComplete(result);
						}
					}
				});
			}
		});
	}


	/**
	 * 异步的Post请求
	 *
	 * @param urlStr
	 * 				发送请求的 URL
	 * @param params
	 * 				请求参数
	 * @param callBack
	 * 				回调函数
	 */
	public static void doPostAsyn(final String urlStr, final String params, final CallBack callBack){
		executor.execute(new Runnable() {
			@Override
			public void run() {
				final String result = doPost(urlStr, params);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (callBack != null) {
							callBack.onRequestComplete(result);
						}
					}
				});
			}
		});
	}


	/**
	 * Get请求，实现网络访问文件，返回字符串
	 * 
	 * @param urlStr
	 * 			  发送请求的 URL
	 * @return String
	 *
	 */
	public static String doGet(String urlStr) {

		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream bos = null;

		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(3000);
			conn.setConnectTimeout(3000);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				bos = new ByteArrayOutputStream();
				int len = -1;
				byte[] bytes = new byte[1024];

				while ((len = is.read(bytes)) != -1)
				{
					bos.write(bytes, 0, len);
				}
				bos.flush();
				return bos.toString();
			} else {
				throw new RuntimeException("网络连接失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null){
					is.close();
				}
				if (bos != null){
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return null;
	}


	/**
	 * POST请求，实现网络访问文件，返回字符串
	 * 
	 * @param urlStr
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，参数格式:name1=value1&name2=value2
	 * @return String
	 */
	public static String doPost(String urlStr, String param) {

		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream bos = null;

		try {
			URL url = new URL(urlStr);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("charset", "utf-8");
			conn.setReadTimeout(3000);
			conn.setConnectTimeout(3000);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			if (param != null && !param.trim().equals("")) {
				// 获取URLConnection对象对应的输出流
				os = conn.getOutputStream();
				// 发送请求参数
				os.write(param.getBytes());
				// flush输出流的缓冲
				os.flush();
			}
			if (conn.getResponseCode() == 200){
				is = conn.getInputStream();
				bos = new ByteArrayOutputStream();
				int len = -1;
				byte[] bytes = new byte[1024];

				while ((len = is.read(bytes)) != -1) {
					bos.write(bytes, 0, len);
				}
				bos.flush();
				return bos.toString();
			} else {
				throw new RuntimeException("网络连接失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}


}
