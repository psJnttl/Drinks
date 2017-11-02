package base;

import base.domain.Manager;
import base.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import base.repository.EmployeeRepo;
import base.repository.ManagerRepository;

/**
 *
 * @author Pasi
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public void run(String... strings) throws Exception {
        Manager palpatine = managerRepository.save(new Manager("palpatine", "emperor", "ROLE_MANAGER"));
        Manager dodo = managerRepository.save(new Manager("dodo", "joku", "ROLE_MANAGER"));
        employeeRepository.save(new Employee("Frodo", "Baggins", "ring bearer", dodo));
        employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar", dodo));
        employeeRepository.save(new Employee("Darth", "Vader", "Sith Lord", palpatine));
        employeeRepository.save(new Employee("Anakin", "Skywalker", "cropduster", palpatine));
    }

}
