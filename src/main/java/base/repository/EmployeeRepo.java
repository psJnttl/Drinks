package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import base.domain.Employee;

/**
 *
 * @author Pasi
 */
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    
}
