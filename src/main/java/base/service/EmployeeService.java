package base.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import base.repository.EmployeeRepo;
import base.domain.Employee;


/**
 *
 * @author Pasi
 */
@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepo employeeRepository;
    
    public List<EmployeeDto> listAll() {
        List<Employee> emps = employeeRepository.findAll();
         return emps.stream()
                 .map(e -> empToDto(e))
                  .collect(Collectors.toList());
    }
    
    private EmployeeDto empToDto(Employee emp) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(emp.getId());
        dto.setFirstName(emp.getFirstName());
        dto.setLastName(emp.getLastName());
        dto.setManagerName(emp.getManager().getName());
        return dto;
    }
}
