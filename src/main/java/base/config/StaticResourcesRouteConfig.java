package base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author Pasi
 */
@Configuration
public class StaticResourcesRouteConfig extends WebMvcConfigurerAdapter {
    
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
      .addResourceLocations("classpath:/static/")   //  target/classes/static
      .resourceChain(true)
      .addResolver(new StaticPathResolver());
  }
}
