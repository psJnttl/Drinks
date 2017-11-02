package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import base.domain.Manager;

/**
 *
 * @author Pasi
 */
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    
}
