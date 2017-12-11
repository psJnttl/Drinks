package base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import base.command.IngredientAdd;
import base.config.UserIdentification;
import base.domain.Ingredient;
import base.domain.LogEntry;
import base.dto.IngredientDto;
import base.repository.IngredientRepository;
import base.repository.LogEntryRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
public class IngredientAspect {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private LogEntryRepository logEntryRepository;
    
    @Autowired
    private UserIdentification userIdentification;

    @Before("execution(* base.service.IngredientService.deleteIngredient(..))")
    private void deleteIngredient(JoinPoint joinPoint) {
        String username = userIdentification.getNameForLog();
        long id = (long) joinPoint.getArgs()[0];
        Ingredient ing = ingredientRepository.findOne(id);
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("DELETE")
                .targetEntity("ingredient")
                .targetId(id)
                .targetName(ing.getName())
                .build();
        logEntryRepository.save(logEntry);
    }
    
    @AfterReturning( pointcut = "execution(* base.service.IngredientService.addIngredient(..))",
            returning = "result")
    private void addIngredient(JoinPoint joinPoint, Object result) {
        String username = userIdentification.getNameForLog();
        IngredientDto dto = (IngredientDto) result;
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("CREATE")
                .targetEntity("ingredient")
                .targetId(dto.getId())
                .targetName(dto.getName())
                .build();
        logEntryRepository.save(logEntry);
    }

    @Before("execution(* base.service.IngredientService.modifyIngredient(..))")
    private void modifyIngredient(JoinPoint joinPoint) {
        String username = userIdentification.getNameForLog();
        long id = (long) joinPoint.getArgs()[0];
        IngredientAdd ingredientMod = (IngredientAdd) joinPoint.getArgs()[1];
        LogEntry after = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("MODIFY")
                .targetEntity("ingredient")
                .targetId(id)
                .targetName(ingredientMod.getName())
                .build();
        logEntryRepository.save(after);
    }
}
