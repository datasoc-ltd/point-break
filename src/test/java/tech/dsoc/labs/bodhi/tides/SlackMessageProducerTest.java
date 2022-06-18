package tech.dsoc.labs.bodhi.tides;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author sih
 */
@Slf4j
class SlackMessageProducerTest {

  @Test
  void testMessage() {
    String message =
        SlackMessageProducer.produceSlackMessage(
            TestFixtures.tidalEvents(), TestFixtures.expectedRising());
    assertThat(message).isNotNull();
  }
}
