package tech.dsoc.labs.bodhi.tides;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sih
 */
@Getter
@Setter
public class TidalEvent {

  private static final ZoneId EUROPE_LONDON = ZoneId.of("Europe/London");
  private static final ZoneId GMT = ZoneId.of("GMT");

  @JsonProperty(value = "EventType")
  private String eventType;

  @JsonProperty(value = "DateTime")
  @Setter(AccessLevel.NONE)
  private String dateTime;

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  @JsonIgnore
  private LocalDateTime localDateTime; // derived to avoid repitition

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
    this.localDateTime = LocalDateTime.parse(this.dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  @JsonProperty(value = "IsApproximateTime")
  private boolean approximateTime;

  @JsonProperty(value = "Height")
  private Double height;

  @JsonProperty(value = "IsApproximateHeight")
  private boolean approximateHeight;

  @JsonProperty(value = "Filtered")
  private boolean filtered;

  @JsonProperty(value = "Date")
  private String date;

  static String DATE_TIME_PATTERN = "yyyy-MM-ddThh24:mm";

  static final String LOW = "Low";
  static final String HIGH = "High";

  String tide() {
    return (eventType.equals("HighWater")) ? HIGH : LOW;
  }

  String height() {
    DecimalFormat df = new DecimalFormat("0.0m");
    return df.format(height);
  }

  /**
   * @return Tide time as GMT (the UK Hydrographic Office outputs times in GMT)
   */
  ZonedDateTime asGmt() {
    return localDateTime.atZone(GMT);
  }

  String clockTime() {
    TimeZone london = TimeZone.getTimeZone(EUROPE_LONDON);
    LocalDateTime clockTime = localDateTime.plus(Duration.ofMillis(london.getDSTSavings()));
    return clockTime.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

}
