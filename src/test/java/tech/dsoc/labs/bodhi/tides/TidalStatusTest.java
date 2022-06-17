package tech.dsoc.labs.bodhi.tides;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.dsoc.labs.bodhi.tides.TidalEvent.HIGH;
import static tech.dsoc.labs.bodhi.tides.TidalStatus.RISING;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author sih
 */
class TidalStatusTest {

  private TidalEvent[] tidalEvents;

  @BeforeEach
  void setUp() {
    tidalEvents = TestFixtures.tidalEvents();
  }

  @Test
  void checkFixtures() {
    assertThat(tidalEvents).hasSize(4);
  }

  @Test
  @DisplayName("Should understand about slack tide")
  void testSlackTide() {
    TidalStatus slackWhenJustAfterLow =
        new TidalStatus(tidalEvents, TestFixtures.slackTimeNearLow());
    assertThat(slackWhenJustAfterLow.getThisStatus().equals(TidalStatus.SLACK));

    TidalStatus slackWhenJustBeforeHigh =
        new TidalStatus(tidalEvents, TestFixtures.slackTimeNearHigh());
    assertThat(slackWhenJustBeforeHigh.getThisStatus().equals(TidalStatus.SLACK));
  }

  @Test
  @DisplayName("Should handle exact time exception scenario")
  void testMagicCoincidence() {
    TidalStatus exact = new TidalStatus(tidalEvents, TestFixtures.exactTimeMatch());
    assertThat(exact.getNextTime()).isEqualTo("Now!");
  }

  @DisplayName("Should work out when tide is rising")
  @Test
  void testRising() {
    TidalStatus rising = new TidalStatus(tidalEvents, TestFixtures.expectedRising());
    String expectedTime = "14:20";
    String expectedStatus = HIGH;
    assertThat(rising.getThisStatus()).isEqualTo(RISING);
    assertThat(rising.getNextStatus()).isEqualTo(expectedStatus);
    assertThat(rising.getNextTime()).isEqualTo(expectedTime);
  }
}
