package fi.solita.clamav;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.security.Constraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
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

  @Value("${clamd.maxfilesize}")
  private String maxfilesize;

  @Value("${clamd.maxrequestsize}")
  private String maxrequestsize;

  @Bean
  MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(maxfilesize);
    factory.setMaxRequestSize(maxrequestsize);
    return factory.createMultipartConfig();
  }

  @Bean
  public EmbeddedServletContainerCustomizer customizer() {
    return new EmbeddedServletContainerCustomizer() {

      @Override
      public void customize(ConfigurableEmbeddedServletContainer container) {
        if (container instanceof JettyEmbeddedServletContainerFactory) {
          customizeJettyToDisableHttpTrace((JettyEmbeddedServletContainerFactory) container);
        }
      }

      /**
       * TRACE method leads to Cross-Site Tracking (XST) problem so it is wise to disable it.
       *
       * Trick to disable HTTP TRACE requests. So no one has access to the "/" using "TRACE" HTTP method.
       * <security-constraint>
       *   <web-resource-collection>
       *     <web-resource-name>Disable TRACE</web-resource-name>
       *     <url-pattern>/</url-pattern>
       *     <http-method>TRACE</http-method>
       *   </web-resource-collection>
       *   <auth-constraint/>
       * </security-constraint>
       *
       * See e.g. https://github.com/eclipse/jetty.project/blob/jetty-9.2.10.v20150310/jetty-webapp/src/main/config/etc/webdefault.xml#L514-L531
       */
      private void customizeJettyToDisableHttpTrace(JettyEmbeddedServletContainerFactory jetty) {
        jetty.addServerCustomizers(new JettyServerCustomizer() {
          @Override
          public void customize(Server server) {
            Handler originalHandler = server.getHandler();

            Constraint disableTraceConstraint = new Constraint();
            disableTraceConstraint.setName("Disable TRACE");
            disableTraceConstraint.setAuthenticate(true);

            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setConstraint(disableTraceConstraint);
            mapping.setMethod("TRACE");
            mapping.setPathSpec("/");

            Constraint omissionConstraint = new Constraint();
            omissionConstraint.setName("Enable everything but TRACE");
            ConstraintMapping omissionMapping = new ConstraintMapping();
            omissionMapping.setConstraint(omissionConstraint);
            omissionMapping.setMethodOmissions(new String [] {"TRACE"});
            omissionMapping.setPathSpec("/");

            ConstraintSecurityHandler wrappingHandler = new ConstraintSecurityHandler();
            wrappingHandler.addConstraintMapping(mapping);
            wrappingHandler.addConstraintMapping(omissionMapping);
            wrappingHandler.setHandler(originalHandler);

            server.setHandler(wrappingHandler);
          }
        });
      }
    };
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
