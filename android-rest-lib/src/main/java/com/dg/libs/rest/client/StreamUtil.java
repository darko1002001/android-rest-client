package com.dg.libs.rest.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class StreamUtil {

  public static String convertStreamToString(final InputStream is) throws IOException {
    return IOUtils.toString(is, "UTF-8");
  }

  public static DefaultHttpClient generateClient() {
    final HttpParams params = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
    HttpConnectionParams.setSoTimeout(params, 20 * 1000);

    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

    // Create and initialize scheme registry
    final SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry
        .register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schemeRegistry
        .register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

    // Create an HttpClient with the ThreadSafeClientConnManager.
    // This connection manager must be used if more than one thread will
    // be using the HttpClient.
    final ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params,
        schemeRegistry);
    final DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
    return httpClient;
  }
}
