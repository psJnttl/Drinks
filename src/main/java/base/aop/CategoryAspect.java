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

import base.command.CategoryAdd;
import base.domain.Category;
import base.domain.LogEntry;
import base.dto.CategoryDto;
import base.repository.CategoryRepository;
import base.repository.LogEntryRepository;

@Aspect
public class CategoryAspect {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private LogEntryRepository logEntryRepository;

    @Before("execution(* base.service.CategoryService.deleteCategory(..))")
    private void deleteCategory(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        Category cat = categoryRepository.findOne(id);
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("DELETE")
                .targetEntity("category")
                .targetId(id)
                .targetName(cat.getName())
                .build();
        logEntryRepository.save(logEntry);
    }
    
    @AfterReturning( pointcut = "execution(* base.service.CategoryService.addCategory(..))",
            returning = "result")
    private void addCategory(JoinPoint joinPoint, Object result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        CategoryDto dto = (CategoryDto) result;
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("CREATE")
                .targetEntity("category")
                .targetId(dto.getId())
                .targetName(dto.getName())
                .build();
        logEntryRepository.save(logEntry);
    }

    @Before("execution(* base.service.CategoryService.modifyCategory(..))")
    private void modifyCategory(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        CategoryAdd categoryMod = (CategoryAdd) joinPoint.getArgs()[1];
        LogEntry after = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("MODIFY")
                .targetEntity("category")
                .targetId(id)
                .targetName(categoryMod.getName())
                .build();
        logEntryRepository.save(after);
    }

}
