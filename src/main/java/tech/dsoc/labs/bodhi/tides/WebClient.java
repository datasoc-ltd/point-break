package tech.dsoc.labs.bodhi.tides;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sih
 */
@Slf4j
public class WebClient {

  private final String webhookUri;
  private final String apiKeyHeader;
  private final String apiKey;

  public WebClient() {
    this.webhookUri = System.getenv("WEBHOOK_URI");
    this.apiKeyHeader = System.getenv("OCP_HEADER");
    this.apiKey = System.getenv("API_KEY");
  }

  CompletableFuture<HttpResponse<String>> tides(String uriAsString) {
    URI uri;
    try {
      uri = new URI(uriAsString);
      HttpRequest request =
          HttpRequest.newBuilder().uri(uri).header(apiKeyHeader, apiKey).GET().build();
      return HttpClient.newBuilder().build().sendAsync(request, BodyHandlers.ofString());
    } catch (URISyntaxException se) {
      log.error(String.format("Invalid URI supplied: %s", uriAsString));
    }
    return null;
  }

  void notifySlack(final String json) {
    try {
      URI bodhiWebhook = new URI(webhookUri);
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(bodhiWebhook)
              .header("Content-Type", "application/json")
              .POST(BodyPublishers.ofString(json))
              .build();
      CompletableFuture<HttpResponse<String>> future =
          HttpClient.newBuilder().build().sendAsync(request, BodyHandlers.ofString());
      HttpResponse<String> response = future.get(5000, TimeUnit.MILLISECONDS);
      if (response.statusCode() != 200) {
        log.error(json);
        log.error("Failed to notify slack. Status code was: " + response.statusCode());
      }
    } catch (URISyntaxException uri) {
      log.error(String.format("Error parsing webhook URI: %s", webhookUri));
    } catch (Exception e) {
      log.error(
          String.format(
              "Error posting to Slack channel. Received exception: %s", e.getClass().toString()));
    }
  }

  private HttpRequest buildRequestTides(URI uri) throws URISyntaxException {
    return HttpRequest.newBuilder().uri(uri).build();
  }
}
