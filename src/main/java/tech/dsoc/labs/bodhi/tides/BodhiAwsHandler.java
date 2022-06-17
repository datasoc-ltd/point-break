package tech.dsoc.labs.bodhi.tides;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sih
 */
@Slf4j
public class BodhiAwsHandler implements RequestStreamHandler {

  static final String TIDES =
      "https://admiraltyapi.azure-api.net/uktidalapi-foundation/api/V2/Stations/0081/TidalEvents?duration=1";

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    WebClient client = new WebClient();
    CompletableFuture<HttpResponse<String>> pageFuture = client.tides(TIDES);
    try {
      HttpResponse<String> page = pageFuture.get(7000, TimeUnit.MILLISECONDS);
      ResponseHandler responseHandler = new ResponseHandler();
      TidalEvent[] tidalEvents = responseHandler.getTideTimes(page.body());
      String message = SlackMessageProducer.produceSlackMessage(tidalEvents);
      client.notifySlack(message);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error retrieving tide times. Exception received was: %s", e.getClass().toString()));
    }
  }
}
