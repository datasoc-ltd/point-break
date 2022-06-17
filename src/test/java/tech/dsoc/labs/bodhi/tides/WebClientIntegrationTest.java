package tech.dsoc.labs.bodhi.tides;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author sih
 */
class WebClientIntegrationTest {

  private WebClient fetcher;
  private static final int HTTP_OK = 200;

  @BeforeEach
  void setUp() {
    fetcher = new WebClient();
  }

  @DisplayName("Given a valid URL when fetchAsync should return a future with an OK response")
  @Test
  void testValidUrl() throws Exception {
    CompletableFuture<HttpResponse<String>> future = fetcher.tides(BodhiAwsHandler.TIDES);
    HttpResponse<String> response = future.get(5000, TimeUnit.MILLISECONDS);
    assertThat(response.statusCode()).isEqualTo(HTTP_OK);
  }
}
