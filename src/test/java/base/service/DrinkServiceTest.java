package base.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import base.domain.Category;
import base.domain.Drink;
import base.domain.Glass;
import base.domain.Ingredient;
import base.dto.DrinkDto;
import base.repository.CategoryRepository;
import base.repository.DrinkRepository;
import base.repository.GlassRepository;
import base.repository.IngredientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DrinkServiceTest {

    @Autowired
    private DrinkService drinkService;
    
    @Autowired
    private DrinkRepository drinkRepository;
    
    @Autowired
    private GlassRepository glassRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;
    
    private static final String CATEGORY_NAME = "hnygbUTFvdytd";
    private static final String GLASS_NAME = "fgtVYDYRVtsttse";
    private static final String INGREDIENT1_NAME = "bhjBFIFT";
    private static final String INGREDIENT2_NAME = "knhygITB";
    private static final String INGREDIENT3_NAME = "IBGFtufbtyd";
    private static final String INGREDIENT4_NAME = "HYIgbtfbuyf";
    private static final String INGREDIENT5_NAME = "JKHBGJdtskj";
    private static final String INGREDIENT_AMOUNT = "42";
    private static final String DRINK1_NAME = "Nhjtbydrvt";
    private static final String DRINK2_NAME = "YRDBTYRVDd";
    private static final String DRINK3_NAME = "rxsawxerax";
    private static final String DRINK4_NAME = "moikjhning";
    private static final String DRINK5_NAME = "yuhuybesan";
    private static final String DRINK6_NAME = "Lmhntytyfb";
    private static final String DRINK7_NAME = "ASrxcvhuhn";
    private Category cat1;
    private Glass gls1;
    private Ingredient ing1, ing2, ing3, ing4, ing5;
    private Drink drink1, drink2, drink3, drink4, drink5, drink6, drink7;

    private Category insertCategory(String name) {
        Category cat = new Category(name);
        cat = categoryRepository.saveAndFlush(cat);
        return cat;
    }

    private Glass insertGlass(String name) {
        Glass glass = new Glass(name);
        glass = glassRepository.saveAndFlush(glass);
        return glass;
    }
    
    private Ingredient insertIngredient(String name) {
        Ingredient ing = new Ingredient(name);
        ing = ingredientRepository.saveAndFlush(ing);
        return ing;
    }
    
    private Drink insertDrink(String name, Category cat, Glass glass, List<Ingredient> ingredients) {
        List<Category> cats = categoryRepository.findAll();
        List<Glass> glasses = glassRepository.findAll();
        List<Ingredient> ings = ingredientRepository.findAll();
        List<Drink>drinks = drinkRepository.findAll();
        Drink drink = new Drink(name);
        drink.setCategory(cat);
        drink.setGlass(glass);
        for (Ingredient ing: ingredients) {
            drink.addIngredient(ing, INGREDIENT_AMOUNT);
        }
        drink = drinkRepository.saveAndFlush(drink);
        return drink;
    }
    
    @Before
    public void setUp() throws Exception {
        cat1 = insertCategory(CATEGORY_NAME);
        gls1 = insertGlass(GLASS_NAME);
        ing1 = insertIngredient(INGREDIENT1_NAME);
        ing2 = insertIngredient(INGREDIENT2_NAME);
        ing3 = insertIngredient(INGREDIENT3_NAME);
        ing4 = insertIngredient(INGREDIENT4_NAME);
        ing5 = insertIngredient(INGREDIENT5_NAME);
        drink1 = insertDrink(DRINK1_NAME, cat1, gls1, Arrays.asList(ing1, ing3));
        drink2 = insertDrink(DRINK2_NAME, cat1, gls1, Arrays.asList(ing1));
        drink3 = insertDrink(DRINK3_NAME, cat1, gls1, Arrays.asList(ing2, ing3));
        drink4 = insertDrink(DRINK4_NAME, cat1, gls1, Arrays.asList(ing1, ing2));
        drink5 = insertDrink(DRINK5_NAME, cat1, gls1, Arrays.asList(ing1, ing2, ing3));
        drink6 = insertDrink(DRINK6_NAME, cat1, gls1, Arrays.asList(ing4, ing2, ing1));
        drink7 = insertDrink(DRINK7_NAME, cat1, gls1, Arrays.asList(ing2, ing4));
    }

    @After
    public void tearDown() throws Exception {
        drinkRepository.delete(drink1);
        drinkRepository.delete(drink2);
        drinkRepository.delete(drink3);
        drinkRepository.delete(drink4);
        drinkRepository.delete(drink5);
        drinkRepository.delete(drink6);
        drinkRepository.delete(drink7);
        ingredientRepository.delete(ing1);
        ingredientRepository.delete(ing2);
        ingredientRepository.delete(ing3);
        ingredientRepository.delete(ing4);
        ingredientRepository.delete(ing5);
        glassRepository.delete(gls1);
        categoryRepository.delete(cat1);
    }

    @Test
    public void ingredient1IsUsedInDrinks() {
        assertTrue(drinkService.isIngredientUsedInDrink(ing1));
    }

    @Test
    public void ingredient5IsNotUsedInDrinks() {
        assertFalse(drinkService.isIngredientUsedInDrink(ing5));
    }

    @Test
    public void originalSolutionMustFind3withIngredient3() {
        List<Drink> list = drinkService.findByIngredient(ing3);
        assertEquals(3, list.size());
    }
    
    @Test
    public void queryDslSolutionMustFind3withIngredient3() {
        List<DrinkDto> list = drinkService.findByIngredientQD(ing3);
        assertEquals(3, list.size());
    }

    @Test
    public void mustNotFindDrinkWithIngredient5() {
        List<DrinkDto> list = drinkService.findByIngredientQD(ing5);
        assertTrue(list.isEmpty());
    }
}
