package base;

import base.domain.Manager;
import base.domain.Role;
import base.domain.Account;
import base.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import base.repository.AccountRepository;
import base.repository.EmployeeRepo;
import base.repository.ManagerRepository;
import base.repository.RoleRepository;

/**
 *
 * @author Pasi
 */
@Profile("test")
@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
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
        if (null == roleRepository.findByName("USER")) {
            Role role = new Role("USER");
            role = roleRepository.saveAndFlush(role);
        }
        if (null == accountRepository.findByUsername("user")) {
            Account userAccount = new Account();
            userAccount.setUsername("user");
            userAccount.setPassword(passwordEncoder.encode("password"));
            Role role = roleRepository.findByName("USER");
            userAccount.addRole(role);
            userAccount = accountRepository.saveAndFlush(userAccount);
        }
        if (null == roleRepository.findByName("ADMIN")) {
            Role admin = new Role("ADMIN");
            admin = roleRepository.saveAndFlush(admin);
        }
        if (null == accountRepository.findByUsername("admin")) {
            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword(passwordEncoder.encode(("admin")));
            Role user = roleRepository.findByName("USER");
            adminAccount.addRole(user);
            Role admin = roleRepository.findByName("ADMIN");
            adminAccount.addRole(admin);
            adminAccount = accountRepository.saveAndFlush(adminAccount);
        }
    }

}
