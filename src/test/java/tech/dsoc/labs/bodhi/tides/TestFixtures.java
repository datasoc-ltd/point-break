package tech.dsoc.labs.bodhi.tides;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author sih
 */
class TestFixtures {

  private static ObjectMapper objectMapper = new ObjectMapper();

  /*
    [
    {
      "EventType": "HighWater",
      "DateTime": "2022-06-17T00:45:00",
      "IsApproximateTime": false,
      "Height": 6.3052100734102474,
      "IsApproximateHeight": false,
      "Filtered": false,
      "Date": "2022-06-17T00:00:00"
    },
    {
      "EventType": "LowWater",
      "DateTime": "2022-06-17T07:05:00",
      "IsApproximateTime": false,
      "Height": 0.54085912212332565,
      "IsApproximateHeight": false,
      "Filtered": false,
      "Date": "2022-06-17T00:00:00"
    },
    {
      "EventType": "HighWater",
      "DateTime": "2022-06-17T13:20:00",
      "IsApproximateTime": false,
      "Height": 6.2786095769363612,
      "IsApproximateHeight": false,
      "Filtered": false,
      "Date": "2022-06-17T00:00:00"
    },
    {
      "EventType": "LowWater",
      "DateTime": "2022-06-17T19:30:00",
      "IsApproximateTime": false,
      "Height": 0.77615320850064573,
      "IsApproximateHeight": false,
      "Filtered": false,
      "Date": "2022-06-17T00:00:00"
    }
  ]
     */
  static TidalEvent[] tidalEvents() {
    try {
      return objectMapper.readValue(
          new File("./src/test/resources/hove-2022-06-17.json"), TidalEvent[].class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static ZonedDateTime exactTimeMatch() {
    return toZdt("2022-06-17T13:20:00");
  }

  static ZonedDateTime slackTimeNearLow() {
    return toZdt("2022-06-17T07:15:00");
  }

  static ZonedDateTime slackTimeNearHigh() {
    return toZdt("2022-06-17T12:55:00");
  }

  static ZonedDateTime expectedRising() {
    return toZdt("2022-06-17T07:36:00");
  }

  static ZonedDateTime expectedFalling() {
    return toZdt("2022-06-17T13:51:00");
  }

  private static ZonedDateTime toZdt(String s) {
    LocalDateTime ldt = LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    return ZonedDateTime.of(ldt, ZoneId.of("UTC"));
  }
}
