package com.dg.libs.rest.client;

import com.dg.libs.rest.RestClientConfiguration;
import com.squareup.okhttp.OkHttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.apache.http.HttpVersion.HTTP_1_1;

/**
 * Created by Fred on 9-12-13.
 */
public class ExtendedOkHttpClient {
  protected final OkHttpClient client;

  int connectionTimeout;
  int socketTimeout;

  public ExtendedOkHttpClient() {
    this(new OkHttpClient());
  }

  public ExtendedOkHttpClient(OkHttpClient client) {
    this.client = client;


    connectionTimeout = RestClientConfiguration.get().getConnectionTimeout();
    socketTimeout = RestClientConfiguration.get().getSocketTimeout();
  }

  protected HttpURLConnection openConnection(URL url) {
    HttpURLConnection openConnection = client.open(url);
    openConnection.setConnectTimeout(connectionTimeout);
    openConnection.setReadTimeout(socketTimeout);
    return openConnection;
  }

  public HttpResponse execute(HttpUriRequest request) throws IOException {
    return execute(null, request, (HttpContext) null);
  }

  public HttpResponse execute(HttpHost host, HttpRequest request, HttpContext context)
      throws IOException {
    // Prepare the request headers.
    RequestLine requestLine = request.getRequestLine();
    URL url = new URL(requestLine.getUri());
    HttpURLConnection connection = openConnection(url);
    connection.setRequestMethod(requestLine.getMethod());
    for (Header header : request.getAllHeaders()) {
      connection.addRequestProperty(header.getName(), header.getValue());
    }

    // Stream the request body.
    if (request instanceof HttpEntityEnclosingRequest) {
      HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
      if (entity != null) {
        connection.setDoOutput(true);
        Header type = entity.getContentType();
        if (type != null) {
          connection.addRequestProperty(type.getName(), type.getValue());
        }
        Header encoding = entity.getContentEncoding();
        if (encoding != null) {
          connection.addRequestProperty(encoding.getName(), encoding.getValue());
        }
        if (entity.isChunked() || entity.getContentLength() < 0) {
          connection.setChunkedStreamingMode(0);
        } else if (entity.getContentLength() <= 8192) {
          // Buffer short, fixed-length request bodies. This costs memory, but permits the request
          // to be transparently retried if there is a connection failure.
          connection.addRequestProperty("Content-Length", Long.toString(entity.getContentLength()));
        } else {
          connection.setFixedLengthStreamingMode((int) entity.getContentLength());
        }
        entity.writeTo(connection.getOutputStream());
      }
    }

    // Read the response headers.
    int responseCode = connection.getResponseCode();
    String message = connection.getResponseMessage();
    BasicHttpResponse response = new BasicHttpResponse(HTTP_1_1, responseCode, message);
    // Get the response body ready to stream.
    InputStream responseBody =
        responseCode < HttpURLConnection.HTTP_BAD_REQUEST ? connection.getInputStream()
            : connection.getErrorStream();
    InputStreamEntity entity = new InputStreamEntity(responseBody, connection.getContentLength());
    for (int i = 0; true; i++) {
      String name = connection.getHeaderFieldKey(i);
      if (name == null) {
        break;
      }
      BasicHeader header = new BasicHeader(name, connection.getHeaderField(i));
      response.addHeader(header);
      if (name.equalsIgnoreCase("Content-Type")) {
        entity.setContentType(header);
      } else if (name.equalsIgnoreCase("Content-Encoding")) {
        entity.setContentEncoding(header);
      }
    }
    response.setEntity(entity);

    return response;
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

  public OkHttpClient getClient() {
    return client;
  }

}
