package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

}
