package tech.dsoc.labs.bodhi.tides;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sih
 */
@Slf4j
class SlackMessageProducer {

  private static final String HEADER = ":beach_with_umbrella: *Hove* - *%s* :beach_with_umbrella:";
  private static final String BLOCK_FORMAT = "{\"blocks\": [%s]}";

  static String produceSlackMessage(TidalEvent[] tidalEvents) {
    return produceSlackMessage(tidalEvents, ZonedDateTime.now());
  }

  static String produceSlackMessage(TidalEvent[] tidalEvents, ZonedDateTime statusAt) {
    String dow = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE"));
    StringBuilder sections = new StringBuilder();
    sections.append(slackSection(String.format(HEADER, dow)));
    sections.append(",");
    sections.append(times(tidalEvents));
    sections.append(",");
    sections.append(status(tidalEvents, statusAt));
    String message = String.format(BLOCK_FORMAT, sections);
    return message;
  }

  private static String times(TidalEvent[] tidalEvents) {
    StringBuilder message = new StringBuilder();
    for (int i = 0; i < tidalEvents.length; i++) {
      StringBuilder line = new StringBuilder();
      TidalEvent next = tidalEvents[i];
      line.append(emojiDirection(next.tide()));
      line.append("  ");
      line.append(next.tide());
      line.append("  *");
      line.append(next.clockTime());
      line.append("*  ");
      line.append(next.height());

      message.append(slackSection(line.toString()));
      if (i != tidalEvents.length - 1) {
        message.append(",");
      }
    }
    return message.toString();
  }

  static String status(TidalEvent[] tidalEvents, ZonedDateTime statusAt) {
    StringBuilder message = new StringBuilder();
    TidalStatus status = new TidalStatus(tidalEvents, statusAt);
    StringBuilder statusMessage = new StringBuilder();
    statusMessage.append("The tide is ");
    statusMessage.append(status.getThisStatus().toLowerCase(Locale.ROOT));
    statusMessage.append(emojiDelta(status.getThisStatus()));
    message.append(slackSection(statusMessage.toString()));
    message.append(",");
    StringBuilder nextTide = new StringBuilder();
    nextTide.append(
        String.format("Next *%s* tide at *%s*", status.getNextStatus(), status.getNextTime()));
    message.append(slackSection(nextTide.toString()));
    return message.toString();
  }

  private static String slackSection(String text) {
    return String.format(
        "{\"type\": \"section\", \"text\": {\"type\": \"mrkdwn\", \"text\": \"%s\"}}", text);
  }

  private static String emojiDirection(String tideType) {
    return tideType.equals(TidalEvent.HIGH) ? ":ocean: :ocean: :ocean:" : ":ocean:";
  }

  private static String emojiDelta(String status) {
    return status.equals(TidalStatus.RISING) ? " :point_up_2:" : " :point_down:";
  }
}
