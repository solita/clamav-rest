package fi.solita.clamav;

import fi.solita.clamav.mock.ClamAVClientFactoryMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfiguration {

  @Bean
  @Primary
  public ClamAVClientFactoryMock clamAVClientFactory() {
      return new ClamAVClientFactoryMock();
  }
}