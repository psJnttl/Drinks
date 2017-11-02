package base.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import base.service.EmployeeDto;
import base.service.EmployeeService;

/**
 *
 * @author Pasi
 */
@RestController
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
            
    @RequestMapping(value = "/api/employees", method = RequestMethod.GET)
    public List<EmployeeDto> listAll() {
        return employeeService.listAll();
    }
}
