package com.framelib.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author caozhifei
 * @version 创建时间：2015-3-28 下午3:42:46 httpclient4工具类
 */
public class HttpClient4Util {
	private static Logger log = LoggerFactory.getLogger(HttpClient4Util.class);

	private static void setHttpClient(HttpClient httpclient) {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.getConnectionManager().closeIdleConnections(10,
				TimeUnit.SECONDS);
		httpclient.getParams().setParameter(AllClientPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		httpclient.getParams().setParameter(
				AllClientPNames.USE_EXPECT_CONTINUE, Boolean.TRUE);
		httpclient.getParams().setParameter(
				AllClientPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
		// httpclient.getParams().setParameter(AllClientPNames.CONN_MANAGER_TIMEOUT,1000);
		httpclient.getParams().setParameter(AllClientPNames.CONNECTION_TIMEOUT,
				4000);
		httpclient.getParams().setParameter(AllClientPNames.SO_TIMEOUT, 5000);

	}

	/**
	 * 发送Get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, Map<String, String> map) {
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (null != map) {
			for (String key : map.keySet()) {
				NameValuePair nvp = new BasicNameValuePair(key, map.get(key));
				nvps.add(nvp);
			}
		}
		setHttpClient(httpclient);
		String body = null;
		HttpGet httpget = new HttpGet(url);
		try {
			// Get请求
			// 设置参数
			String str = EntityUtils.toString(new UrlEncodedFormEntity(nvps));
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			// 发送请求
			HttpResponse httpresponse = httpclient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (ParseException e) {
			
			log.error(e.toString(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.toString(), e);
		} catch (IOException e) {
			log.error(e.toString(), e);
		} catch (URISyntaxException e) {
			log.error(e.toString(), e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	/**
	 * 发送Get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		setHttpClient(httpclient);
		String body = null;
		// if(!CharTools.isUtf8Url(url)){
		// url = CharTools.Utf8URLencode(url);
		// }
		HttpGet httpget = new HttpGet(url);
		try {
			// Get请求
			// 设置参数
			// String str = EntityUtils.toString(new
			// UrlEncodedFormEntity(params));
			// httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			httpget.setURI(new URI(url));
			// 发送请求
			HttpResponse httpresponse = httpclient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (ParseException e) {
			log.error(e.toString(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.toString(), e);
		} catch (IOException e) {
			log.error(e.toString(), e);
		} catch (URISyntaxException e) {
			log.error(e.toString(), e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 */
	public static String post(String url, Map<String, String> map) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (null != map) {
			for (String key : map.keySet()) {
				NameValuePair nvp = new BasicNameValuePair(key, map.get(key));
				nvps.add(nvp);
			}
		}
		String html = null;
		String result = "";
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = null;
		HttpClient httpclient = new DefaultHttpClient();
		setHttpClient(httpclient);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpResponse = httpclient.execute(httpPost);
			BufferedInputStream buffer = new BufferedInputStream(httpResponse
					.getEntity().getContent());
			byte[] bytes = new byte[1024];
			int line = 0;
			StringBuilder builder = new StringBuilder();
			while ((line = buffer.read(bytes)) != -1) {
				builder.append(new String(bytes, 0, line, "UTF-8"));
			}
			result = builder.toString();
		} catch (Exception e) {
			log.error(e.toString(), e);
		} finally {
			if (httpPost.isAborted()) {
				httpPost.abort();
			}
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("name", "123");
		// http://127.0.0.1:8080/baoogu/index.htm
		// String html = HttpClient4Util.get(
		// "http://127.0.0.1:8080/baoogu/index.htm");
		// String message = "范围是否";
		// message = CharTools.Utf8URLencode(message);
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			String html = HttpClient4Util.get(
					"http://192.168.6.105:8080/member/test/login.htm", map);
			// System.out.println(html);
			if (i % 1000 == 0) {
				System.out.println(i);
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("time@@@@####:" + (t2 - t1));
	}

}
