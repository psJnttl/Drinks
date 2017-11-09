package base.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value="classpath:aop.xml")
public class AopConfiguration {

}
