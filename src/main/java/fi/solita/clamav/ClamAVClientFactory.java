package fi.solita.clamav;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClamAVClientFactory {

  private final String hostname;
  private final int port;
  private final int timeout;

  public ClamAVClientFactory(@Value("${clamd.host}") String hostname,
                             @Value("${clamd.port}") int port,
                             @Value("${clamd.timeout}") int timeout) {
    this.hostname = hostname;
    this.port = port;
    this.timeout = timeout;
  }

  public ClamAVClient newClient() {
        return new ClamAVClient(hostname, port, timeout);
    }
}
