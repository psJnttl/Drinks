package base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Aspect
public class IngredientAspect {

    private Log log = LogFactory.getLog(this.getClass());
    
    @Before("execution(* base.service.IngredientService.listAll(..))")
    private void listIngredients(JoinPoint jointPoint) {
        log.info("listing ingredients");
    }
}
