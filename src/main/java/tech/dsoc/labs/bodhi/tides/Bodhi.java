package tech.dsoc.labs.bodhi.tides;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sih
 */
@Slf4j
public class Bodhi {

  public static void main(String[] args) {
    BodhiAwsHandler handler = new BodhiAwsHandler();
    try {
      handler.handleRequest(null, null, null);
    } catch (IOException ioe) {
      log.error("Couldn't invoke handler manually");
    }

  }
}
