package tech.dsoc.labs.bodhi.tides;

import static tech.dsoc.labs.bodhi.tides.BodhiException.EVENTS_IN_PAST;
import static tech.dsoc.labs.bodhi.tides.BodhiException.NEED_TO_SUPPLY_EVENTS;
import static tech.dsoc.labs.bodhi.tides.TidalEvent.LOW;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author sih
 */
@Getter(AccessLevel.PACKAGE)
class TidalStatus {
  static final String RISING = "Rising";
  static final String FALLING = "Falling";
  static final String SLACK = "Slack";

  private String thisStatus;
  private String nextTime;
  private String nextStatus;

  private final ZonedDateTime statusAt;

  @Getter(AccessLevel.NONE)
  private final TidalEvent[] ascOrderedEvents;

  private static final TemporalAmount SLACK_THRESHOLD = Duration.ofMinutes(30);

  TidalStatus(TidalEvent[] ascOrderedEvents, ZonedDateTime statusAt) {
    this.ascOrderedEvents = ascOrderedEvents;
    this.statusAt = statusAt;
    validate();
    int lastIndex = 0, nextIndex = 0;
    for (int i = 0; i < ascOrderedEvents.length; i++) {
      ZonedDateTime thisTime = ascOrderedEvents[i].asGmt();
      if (statusAt.isEqual(thisTime)) {
        lastIndex = i;
        nextIndex = i;
        break;
      } else if (statusAt.isAfter(thisTime)) {
        lastIndex = i;
      } else {
        nextIndex = i;
        break;
      }
    }
    calculateStatus(lastIndex, nextIndex);
  }

  TidalStatus(TidalEvent[] ascOrderedEvents) {
    this(ascOrderedEvents, ZonedDateTime.now());
  }

  private void validate() {
    if (null == ascOrderedEvents) {
      throw new BodhiException(NEED_TO_SUPPLY_EVENTS);
    }
    if (ascOrderedEvents.length == 0) {
      throw new BodhiException(NEED_TO_SUPPLY_EVENTS);
    }
    if (ascOrderedEvents[ascOrderedEvents.length - 1].asGmt().isBefore(statusAt)) {
      throw new BodhiException(EVENTS_IN_PAST);
    }
  }

  private void calculateStatus(int lastIndex, int nextIndex) {
    if (lastIndex == nextIndex) {
      this.thisStatus = SLACK;
      this.nextStatus = ascOrderedEvents[nextIndex].tide();
      this.nextTime = "Now!";
    } else {
      TidalEvent last = ascOrderedEvents[lastIndex];
      TidalEvent next = ascOrderedEvents[nextIndex];
      if (statusAt.minus(SLACK_THRESHOLD).isBefore(last.asGmt())
          || statusAt.plus(SLACK_THRESHOLD).isAfter(next.asGmt())) {
        thisStatus = SLACK;
      } else if ((last.tide()).equals(LOW)) {
        this.thisStatus = RISING;
      } else {
        this.thisStatus = FALLING;
      }
      this.nextTime = next.clockTime();
      this.nextStatus = next.tide();
    }
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("The tide is currently ")
        .append(thisStatus.toLowerCase(Locale.ROOT))
        .append(" and the next ")
        .append(nextStatus)
        .append(" tide is at ")
        .append(nextTime)
        .toString();
  }
}
