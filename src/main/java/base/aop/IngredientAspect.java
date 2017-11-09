package base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import base.domain.Ingredient;
import base.repository.IngredientRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Aspect
public class IngredientAspect {

    @Autowired
    private IngredientRepository ingredientRepository;

    private Log log = LogFactory.getLog(this.getClass());

    @Before("execution(* base.service.IngredientService.deleteIngredient(..))")
    private void deleteIngredient(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        Ingredient ingredient = ingredientRepository.findOne(id);
        log.info(username + " DELETE ingredient id: " + id + ", name: " + ingredient.getName());
    }
}
