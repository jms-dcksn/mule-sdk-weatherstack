package org.mule.extension.internal;


import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class WeatherConnection {

  public static final String CITY = "Get weather by CITY";

  //public static final String apiKey = "549aed2fa4befeb0bf1093fa9e62d899";
  public static final String baseUrl = "http://api.weatherstack.com/";

  private int connectionTimeout;
  private String apiKey;
  private HttpClient httpClient;
  private HttpRequestBuilder httpRequestBuilder;

  public static MultiMap<String, String> getQueryForCity(String city, String units, String apiKey) {
    MultiMap<String, String> q = new MultiMap<String, String>();
    q.put("access_key", apiKey);
    q.put("query", city);
    q.put("units", units);
    return q;
  }

  public WeatherConnection(HttpService httpService, int cTimeout, String apiKey) {
    this.apiKey = apiKey;
    this.connectionTimeout = cTimeout;
    initHttpClient(httpService);

  }

  public void initHttpClient (HttpService httpService) {
    HttpClientConfiguration.Builder builder = new HttpClientConfiguration.Builder();
    builder.setName("WeatherStack");
    httpClient = httpService.getClientFactory().create(builder.build());
    httpRequestBuilder = HttpRequest.builder();
    httpClient.start();
  }

//  public String getId() {
//    return id;
//  }

  public void invalidate() {
    httpClient.stop();
  }

  public boolean isConnected() throws Exception{

    MultiMap<String, String> qParams = getQueryForCity("Dallas", "f", apiKey);

    HttpRequest request = httpRequestBuilder
            .method(HttpConstants.Method.POST)
            .uri(baseUrl + "current")
            .queryParams(qParams)
            .build();

    HttpResponse httpResponse = httpClient.send(request,connectionTimeout,false,null);

    if (httpResponse.getStatusCode() >= 200 && httpResponse.getStatusCode() < 300)
      return true;
    else
      throw new ConnectionException("Error connecting to the server: Error Code " + httpResponse.getStatusCode()
              + "~" + httpResponse);
  }

  public InputStream callHttpCITY(String city, String units) {
    HttpResponse httpResponse = null;
    String strUri = baseUrl + "current";
    System.out.println("URL is: " + strUri);
    MultiMap<String, String> qParams = getQueryForCity(city, units, apiKey);
    HttpRequest request = httpRequestBuilder
            .method(HttpConstants.Method.GET)
            .uri(strUri)
            .queryParams(qParams)
            .build();
    System.out.println("Request is: " + request);
    try {
      httpResponse = httpClient.send(request,5000,false,null);
      System.out.println(httpResponse);
      return httpResponse.getEntity().getContent();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TimeoutException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
