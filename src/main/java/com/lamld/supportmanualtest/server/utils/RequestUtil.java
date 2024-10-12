package com.lamld.supportmanualtest.server.utils;

import com.lamld.supportmanualtest.server.data.pojo.KeyValue;
import com.lamld.supportmanualtest.server.data.pojo.RequestResponse;
import com.lamld.supportmanualtest.server.exception.InternalException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class RequestUtil {
  private static final HttpClient httpClient = HttpClient.newHttpClient();

  // Utility method to send requests with specified method
  public static RequestResponse sendRequest(String url, String method, Object body, List<KeyValue> headers, List<KeyValue> queryParams) {
    try {
      // Build URL with query parameters if method is GET
      if (queryParams != null && !queryParams.isEmpty()) {
        url = buildUrlWithParams(url, queryParams);
      }
      String jsonBody = JsonParser.convertBodyToJson(body);


      HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Content-Type", "application/json");

      // Set the method (GET, POST, PUT, DELETE)
      switch (method.toUpperCase()) {
        case "GET":
          requestBuilder.GET();
          break;
        case "POST":
          requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
          break;
        case "PUT":
          requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
          break;
        case "DELETE":
          requestBuilder.DELETE();
          break;
      }

      // Add headers
      addHeaders(requestBuilder, headers);

      return sendHttpRequest(requestBuilder.build());
    } catch (Exception e) {
      log.error("Error sending request", e);
      throw new InternalException();
    }
  }

  // Utility method to build URL with query parameters
  public static String buildUrlWithParams(String baseUrl, List<KeyValue> queryParams) {
    StringBuilder urlBuilder = new StringBuilder(baseUrl);
    if (queryParams != null && !queryParams.isEmpty()) {
      urlBuilder.append("?");
      queryParams.forEach(param -> {
        try {
          urlBuilder.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8))
              .append("=")
              .append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8))
              .append("&");
        } catch (Exception e) {
          log.error("Error encoding query parameters", e);
        }
      });
      // Remove the trailing '&' if present
      urlBuilder.setLength(urlBuilder.length() - 1);
    }
    return urlBuilder.toString();
  }

  // Utility method to send requests and handle responses
  private static RequestResponse sendHttpRequest(HttpRequest request) throws IOException, InterruptedException {
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    RequestResponse requestResponse = new RequestResponse();
    requestResponse.setStatus(response.statusCode());

    // Parse the response body
    String responseBody = response.body();
    if (responseBody.startsWith("[")) {
      // Handle a list of JSON objects (JSONArray)
      JSONArray jsonArray = new JSONArray(responseBody);
      List<Object> rawList = jsonArray.toList(); // This returns a list of Objects
      List<Map<String, Object>> listOfMaps = rawList.stream()
          .filter(item -> item instanceof Map) // Check if item is a Map
          .map(item -> (Map<String, Object>) item) // Safely cast to Map<String, Object>
          .collect(Collectors.toList());
      requestResponse.setBody(listOfMaps);
    } else if (responseBody.startsWith("{")) {
      // Handle a single JSON object (JSONObject)
      JSONObject jsonObject = new JSONObject(responseBody);
      Map<String, Object> map = jsonObject.toMap();
      requestResponse.setBody(map);
    } else {
      // Handle non-JSON responses or error handling if needed
      requestResponse.setBody(responseBody); // Or handle differently
    }

    return requestResponse;
  }

  // Utility method to add headers to the request
  private static void addHeaders(HttpRequest.Builder requestBuilder, List<KeyValue> headers) {
    if (headers != null) {
      headers.forEach(header -> {
        log.debug("Adding header: {} = {}", header.getKey(), header.getValue());
        requestBuilder.header(header.getKey(), header.getValue());
      });
    }
  }
}
