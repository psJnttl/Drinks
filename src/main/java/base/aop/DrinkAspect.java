package base.aop;

import java.time.LocalDateTime;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import base.command.DrinkAdd;
import base.domain.Drink;
import base.domain.LogEntry;
import base.dto.DrinkDto;
import base.repository.DrinkRepository;
import base.repository.LogEntryRepository;

@Aspect
public class DrinkAspect {

    @Autowired
    private DrinkRepository drinkRepository;
    
    @Autowired
    private LogEntryRepository logEntryRepository;
    
    @Before("execution(* base.service.DrinkService.deleteDrink(..))")
    private void deleteDrink(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        Drink drink = drinkRepository.findOne(id);
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("DELETE")
                .targetEntity("drink")
                .targetId(id)
                .targetName(drink.getName())
                .build();
        logEntryRepository.save(logEntry);
    }
    
    @AfterReturning( pointcut = "execution(* base.service.DrinkService.addDrink(..))",
                     returning = "result")
    private void addDrink(JoinPoint joinPoint, Object result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        DrinkDto drink = (DrinkDto) result;
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("CREATE")
                .targetEntity("drink")
                .targetId(drink.getId())
                .targetName(drink.getName())
                .build();
        logEntryRepository.save(logEntry);
    }
    
    @Before("execution(* base.service.DrinkService.modifyDrink(..))")
    private void modifyDrink(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        DrinkAdd drink = (DrinkAdd) joinPoint.getArgs()[1];
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("MODIFY")
                .targetEntity("drink")
                .targetId(id)
                .targetName(drink.getName())
                .build();
        logEntryRepository.save(logEntry);
    }
}
