package fi.solita.clamav;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@EnableAutoConfiguration
@ComponentScan
/**
 * Simple Spring Boot application which acts as a REST endpoint for
 * clamd server.
 */
public class Application {

  @Value("${clamd.maxfilesize}")
  private String maxfilesize;

  @Value("${clamd.maxrequestsize}")
  private String maxrequestsize;

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.parse(maxfilesize));
    factory.setMaxRequestSize(DataSize.parse(maxrequestsize));
    return factory.createMultipartConfig();
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    Map<String, Object> defaults = new HashMap<String, Object>();
    defaults.put("clamd.host", "192.168.50.72");
    defaults.put("clamd.port", 3310);
    defaults.put("clamd.timeout", 500);
    defaults.put("clamd.maxfilesize", "20000KB");
    defaults.put("clamd.maxrequestsize", "20000KB");
    app.setDefaultProperties(defaults);
    app.run(args);
  }
}
