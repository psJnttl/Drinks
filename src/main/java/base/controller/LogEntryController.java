package base.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.domain.LogEntry;
import base.dto.LogEntryDto;
import base.repository.LogEntryRepository;

@RestController
public class LogEntryController {

    @Autowired
    private LogEntryRepository logEntryRepository;
    
    @RequestMapping(value = "/api/logentries", method = RequestMethod.GET)
    public List<LogEntryDto> listAll() {
        List<LogEntry> logEntries = logEntryRepository.findAll();
        return logEntries.stream()
            .map(l -> LogEntryDto.builder()
                    .id(l.getId())
                    .date(l.getDate())
                    .username(l.getUsername())
                    .action(l.getAction())
                    .targetEntity(l.getTargetEntity())
                    .targetId(l.getTargetId())
                    .targetName(l.getTargetName())
                    .build())
            .collect(Collectors.toList());
    }
}
