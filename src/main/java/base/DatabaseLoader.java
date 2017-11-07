package base;

import base.domain.Manager;
import base.domain.Role;
import base.domain.Account;
import base.domain.Employee;
import base.domain.Ingredient;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import base.repository.AccountRepository;
import base.repository.EmployeeRepo;
import base.repository.IngredientRepository;
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
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception {
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
        if (ingredientRepository.findAll().isEmpty()) {
            String [] ings = {"Vodka", "Mustikkalikööri", "Schweppes russian", "Kuohuviini", "Appelsiinimehu",
                    "Omenaviini", "Puolukkalikööri", "Sitruunalimonadi", "Vadelmalikööri", "Valkoviini", 
                    "Kuohuviini (Rosee)", "Mansikkalikööri", "Viski (Scotch)", "Kirsikkalikööri", "Campari"};
            insertIngredients(Arrays.asList(ings));
            
        }
    }

    private void insertIngredients(List<String> ingredients) {
        for (String name: ingredients) {
            Ingredient ing = new Ingredient(name);
            ingredientRepository.saveAndFlush(ing);
        }
            
    }

}
