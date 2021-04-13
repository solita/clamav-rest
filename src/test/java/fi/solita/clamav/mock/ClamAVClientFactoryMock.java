package fi.solita.clamav.mock;

import fi.solita.clamav.ClamAVClient;
import fi.solita.clamav.ClamAVClientFactory;
import org.mockito.Mockito;

public class ClamAVClientFactoryMock extends ClamAVClientFactory {

  public final ClamAVClient clamAVClientMock = Mockito.mock(ClamAVClient.class);

  public ClamAVClientFactoryMock() {
        super("localhost", 8080, 1);
    }

  @Override
  public ClamAVClient newClient() {
        return clamAVClientMock;
    }
}
