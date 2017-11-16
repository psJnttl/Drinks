package base.aop;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import base.command.GlassAdd;
import base.domain.Glass;
import base.domain.LogEntry;
import base.dto.GlassDto;
import base.repository.GlassRepository;
import base.repository.LogEntryRepository;

@Aspect
public class GlassAspect {

    @Autowired
    private GlassRepository glassRepository;
    
    @Autowired
    private LogEntryRepository logEntryRepository;

    private Log log = LogFactory.getLog(this.getClass());

    @Before("execution(* base.service.GlassService.deleteGlass(..))")
    private void deleteGlass(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        Glass glass = glassRepository.findOne(id);
        log.info(username + " DELETE glass id: " + id + ", name: " + glass.getName());
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("DELETE")
                .targetEntity("glass")
                .targetId(id)
                .targetName(glass.getName())
                .build();
        logEntryRepository.save(logEntry); // transactional?
    }
    
    @AfterReturning( pointcut = "execution(* base.service.GlassService.addGlass(..))",
                     returning = "result")
    private void addGlass(JoinPoint joinPoint, Object result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GlassDto dto = (GlassDto) result;
        LogEntry logEntry = LogEntry.builder()
                .date(LocalDateTime.now())
                .username(username)
                .action("CREATE")
                .targetEntity("glass")
                .targetId(dto.getId())
                .targetName(dto.getName())
                .build();
        logEntryRepository.save(logEntry);
    }
    
    @Before("execution(* base.service.GlassService.modifyGlass(..))")
    private void modifyGlass(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        long id = (long) joinPoint.getArgs()[0];
        GlassAdd glassMod = (GlassAdd) joinPoint.getArgs()[1];
        LocalDateTime now = LocalDateTime.now();
        LogEntry after = LogEntry.builder()
                .date(now)
                .username(username)
                .action("MODIFY")
                .targetEntity("glass")
                .targetId(id)
                .targetName(glassMod.getName())
                .build();
        logEntryRepository.save(after);
    }
    
}
