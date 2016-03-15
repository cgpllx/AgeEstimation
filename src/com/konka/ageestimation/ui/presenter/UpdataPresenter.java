package com.konka.ageestimation.ui.presenter;

import java.io.ByteArrayInputStream;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UpdataPresenter {
	private AsyncHttpClient asyncHttpClient;
	public static final String URL = "http://howold.fatebird.cn/upload/AndroidManifest.xml";

	public void update(final int oldVersionCode, final IUpdateCallback callback) {
		if (asyncHttpClient == null) {
			asyncHttpClient = new AsyncHttpClient();
		}
		asyncHttpClient.cancelAllRequests(true);
		asyncHttpClient.get(URL, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// String string = new String(responseBody, "UTF-8");
				final XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(new ByteArrayInputStream(responseBody), "UTF-8");
					int eventType = parser.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if ("manifest".equals(parser.getName())) {
							String versionCode = parser.getAttributeValue(null, "versionCode");
							int newVersionCode = Integer.parseInt(versionCode);
							System.out.println("versionCode=" + versionCode);
							if (callback != null) {
								callback.transmitResult(newVersionCode > oldVersionCode);
							}
							return;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				callback.onFailure();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// parserJson(null);
			}
		});

	}
}
