package com.dg.libs.rest.client;

import java.net.HttpURLConnection;
import java.net.URL;

import com.squareup.okhttp.apache.OkApacheClient;

public class ExtendedOkApacheClient extends OkApacheClient{

	int connectionTimeout = 5000;
	int socketTimeout = 20000;
	
	@Override
	protected HttpURLConnection openConnection(URL url) {
		HttpURLConnection openConnection = super.openConnection(url);
		openConnection.setConnectTimeout(connectionTimeout);
		openConnection.setReadTimeout(socketTimeout);
		return openConnection;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	
}
