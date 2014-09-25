package fi.solita.clamav;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
  
@Configuration
@EnableAutoConfiguration
@ComponentScan
/**
 * Simple Spring Boot application which acts as a REST endpoint for
 * clamd server. 
 */
public class Application {
  
  @Bean
  MultipartConfigElement multipartConfigElement() {
      MultipartConfigFactory factory = new MultipartConfigFactory();
      factory.setMaxFileSize("20000KB");
      factory.setMaxRequestSize("20000KB");
      return factory.createMultipartConfig();
  }
  
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    Map<String, Object> defaults = new HashMap<String, Object>();
    defaults.put("clamd.host", "192.168.50.72");
    defaults.put("clamd.port", 3310);
    app.setDefaultProperties(defaults);
    app.run(args);
  } 
}
