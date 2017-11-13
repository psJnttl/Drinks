package base;

import base.domain.Role;
import base.domain.Account;
import base.domain.Category;
import base.domain.Drink;
import base.domain.Glass;
import base.domain.Ingredient;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import base.repository.AccountRepository;
import base.repository.CategoryRepository;
import base.repository.DrinkRepository;
import base.repository.GlassRepository;
import base.repository.IngredientRepository;
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
    private GlassRepository glassRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DrinkRepository drinkRepository;

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
            String[] ings = { "Vodka", "Valkoinen vermutti", "Schweppes russian", "Kuohuviini", "Appelsiinimehu",
                    "Ginger ale", "Triple sec", "Sitruunalimonadi", "Vadelmalikööri", "Sokeriliemi", "Viski", 
                    "Greippimehu", "Vaalea rommi", "Viski (Scotch)", "Limemehu", "Campari", "Samppanja" };
            insertIngredients(Arrays.asList(ings));
        }
        if (glassRepository.findAll().isEmpty()) {
            String[] glss = { "Boolimalja", "Cocktail", "Highball", "Hurricane", "Irish Coffee", "Kuohuviini",
                    "Margarita", "On the Rocks", "Shotti", "Valkoviini", "Punaviini" };
            insertGlassware(Arrays.asList(glss));
        }
        if (categoryRepository.findAll().isEmpty()) {
            String[] cats = { "Booli", "Drinkki", "Klassikko", "Kuumat", "Shot" };
            insertCategories(Arrays.asList(cats));
        }/*
        if (drinkRepository.findAll().isEmpty()) {
            String[][] ing = { 
                    { "Vodka", "6 cl", "Valkoinen vermutti", "2 cl" },
                    {"Vodka","2 cl", "Campari", "2 cl", "Samppanja", "10 cl"}, 
                    {"Vodka", "3 cl", "Triple sec", "3 cl", "Limemehu", "3 cl"},
                    {"Viski", "4 cl", "Limemehu", "2 cl", "Ginger ale", "6 cl"},
                    {"Vaalea rommi", "4 cl", "Limemehu", "1cl", "Greippimehu", "1 cl", "Sokeriliemi", "1 cl"}
            };
            insertDrink("Vodka Martini", "Klassikko", "Cocktail", ing[0]);
            insertDrink("Champagne flamingo", "Drinkki", "Kuohuviini", ing[1]);
            insertDrink("Kamikaze", "Drinkki", "Kuohuviini", ing[2]);
            insertDrink("Mobile mule", "Drinkki", "On the Rocks", ing[3]);
            insertDrink("Nevada", "Drinkki", "Cocktail", ing[3]);
        }*/
    }

    private void insertIngredients(List<String> ingredients) {
        for (String name : ingredients) {
            Ingredient ing = new Ingredient(name);
            ingredientRepository.saveAndFlush(ing);
        }
    }

    private void insertGlassware(List<String> glasses) {
        for (String name : glasses) {
            Glass glass = new Glass(name);
            glassRepository.saveAndFlush(glass);
        }
    }

    private void insertCategories(List<String> categories) {
        for (String name : categories) {
            Category cat = new Category(name);
            categoryRepository.saveAndFlush(cat);
        }
    }

    private void insertDrink(String name, String cat, String glss, String[] ingredients) {
        Category category = categoryRepository.findByName(cat);
        Glass glass = glassRepository.findByName(glss);
        Drink drink = new Drink(name);
        drink.setCategory(category);
        drink.setGlass(glass);
        for (int i = 0; i < ingredients.length; i += 2) {
            Ingredient ingredient = ingredientRepository.findByName(ingredients[i]);
            drink.addIngredient(ingredient, ingredients[i + 1]);
        }
        drinkRepository.saveAndFlush(drink);
    }

}
