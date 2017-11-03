package base;

import base.domain.Manager;
import base.domain.Account;
import base.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import base.repository.AccountRepository;
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
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception {
        Manager palpatine = managerRepository.save(new Manager("palpatine", "emperor", "ROLE_MANAGER"));
        Manager dodo = managerRepository.save(new Manager("dodo", "joku", "ROLE_MANAGER"));
        employeeRepository.save(new Employee("Frodo", "Baggins", "ring bearer", dodo));
        employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar", dodo));
        employeeRepository.save(new Employee("Darth", "Vader", "Sith Lord", palpatine));
        employeeRepository.save(new Employee("Anakin", "Skywalker", "cropduster", palpatine));
        if (null != accountRepository.findByUsername("user")) {
            return;
        }
        Account account = new Account();
        account.setUsername("user");
        account.setRole("USER");
        account.setPassword(passwordEncoder.encode("password"));
        account = accountRepository.saveAndFlush(account);
    }

}
