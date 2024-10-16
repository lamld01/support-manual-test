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
  public static RequestResponse sendRequest(String url, String method, Object body, List<KeyValue> headers, List<KeyValue> queryParams, long waitTime) {
    try {
      log.debug("Sending request with url: {}, method: {}, body: {}, headers: {}, queryParams: {}", url, method, body, headers, queryParams);

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
        default:
          throw new IllegalArgumentException("Invalid HTTP method: " + method);
      }

      // Add headers
      addHeaders(requestBuilder, headers);

      // Gửi yêu cầu
      RequestResponse response = sendHttpRequest(requestBuilder.build());
      response.setRequest(body);
      // Thêm thời gian chờ trước khi kết thúc
      if (waitTime > 0) {
        Thread.sleep(waitTime); // Thời gian chờ giữa các lần gọi
      }

      return response;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
      log.error("Thread was interrupted", e);
      throw new InternalException("Request was interrupted");
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
          .map(item -> {
            Map<String, Object> mapItem = (Map<String, Object>) item; // Safely cast to Map<String, Object>
            mapItem.remove("stackTrace"); // Remove the stackTrace key if it exists
            return mapItem;
          }).collect(Collectors.toList());
      requestResponse.setResponse(listOfMaps);
    } else if (responseBody.startsWith("{")) {
      // Handle a single JSON object (JSONObject)
      JSONObject jsonObject = new JSONObject(responseBody);
      Map<String, Object> map = jsonObject.toMap();
      requestResponse.setResponse(map);
    } else {
      // Handle non-JSON responses or error handling if needed
      requestResponse.setResponse(responseBody); // Or handle differently
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
