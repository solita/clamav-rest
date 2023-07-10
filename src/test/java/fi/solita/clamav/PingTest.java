package fi.solita.clamav;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

/**
 * These tests assume clamav-rest Docker container is running and responding
 * locally.
 */
@Disabled
public class PingTest {

  @Test
  public void testPing() {
    RestTemplate t = new RestTemplate();
    String s = t.getForObject("http://localhost:8080", String.class);
    assertEquals(s, "Clamd responding: true\n");
  }
}
