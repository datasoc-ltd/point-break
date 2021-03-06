package tech.dsoc.labs.bodhi.tides;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sih
 */
@Slf4j
class TidesApiResponseHandler {

  private static ObjectMapper objectMapper = new ObjectMapper();
  private TidalEvent[] tidalEvents;

  TidalEvent[] getTideTimes(String data) {
    try {
      tidalEvents = objectMapper.readValue(data, TidalEvent[].class);
    } catch (JsonProcessingException jpe) {
      throw new BodhiException(String.format("Could not parse API response\n%s", data));
    }
    return tidalEvents;
  }
}
