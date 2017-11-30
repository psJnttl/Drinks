package base.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import base.command.DrinkAdd;
import base.domain.Category;
import base.domain.Drink;
import base.domain.DrinkPredicates;
import base.domain.Glass;
import base.domain.Ingredient;
import base.dto.CategoryDto;
import base.dto.DrinkComponent;
import base.dto.DrinkDto;
import base.dto.GlassDto;
import base.dto.IngredientDto;
import base.repository.DrinkRepository;
import base.repository.CategoryRepository;
import base.repository.DrinkQueryDslRepository;
import base.repository.GlassRepository;
import base.repository.IngredientRepository;

@Service
public class DrinkService {

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GlassRepository glassRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private DrinkQueryDslRepository drinkQueryDslRepository;

    @Transactional(readOnly=true)
    public List<Drink> findByIngredient(Ingredient ing) {
        List<Long> list = jdbcTemplate.query(
                "SELECT D.id FROM drink D, ingredient I, drink_ingredients DI "
                        + "WHERE I.id=? AND D.id=DI.drink_id AND I.id = DI.ingredient_id",
                new Object[] { ing.getId(), }, (rs, rowNbr) -> resultExtractor(rs));
        return list.stream().map(i -> drinkRepository.findOne(i)).collect(Collectors.toList());
    }

    private Long resultExtractor(ResultSet rs) throws SQLException {
        return rs.getLong("id");
    }

    @Transactional(readOnly=true)
    public List<Drink> findByGlass(Glass glass) {
        return drinkRepository.findByGlass(glass);
    }

    @Transactional(readOnly=true)
    public List<DrinkDto> listAll() {
        List<Drink> drinks = drinkRepository.findAll();
        return drinks.stream().map(d -> createDto(d)).collect(Collectors.toList());
    }

    private DrinkDto createDto(Drink drink) {
        Category cat = drink.getCategory();
        CategoryDto catDto = new CategoryDto(cat.getId(), cat.getName());
        Glass glass = drink.getGlass();
        GlassDto glassDto = new GlassDto(glass.getId(), glass.getName());
        Map<Ingredient, String> ingredients = drink.getIngredients();
        List<DrinkComponent> components = new ArrayList<>();
        for (Ingredient key : ingredients.keySet()) {
            String value = ingredients.get(key);
            IngredientDto iDto = new IngredientDto(key.getId(), key.getName());
            components.add(new DrinkComponent(iDto, value));
        }
        return new DrinkDto(drink.getId(), drink.getName(), catDto, glassDto, components);
    }

    @Transactional(readOnly=true)
    public List<Drink> findByCategory(Category cat) {
        return drinkRepository.findByCategory(cat);
    }

    @Transactional
    public DrinkDto addDrink(DrinkAdd drinkAdd) {
        Drink drink = new Drink(drinkAdd.getName());
        Category cat = categoryRepository.findOne(drinkAdd.getCategory().getId());
        drink.setCategory(cat);
        Glass glass = glassRepository.findOne(drinkAdd.getGlass().getId());
        drink.setGlass(glass);
        drink.setIngredients(componentsToIngredients(drinkAdd.getComponents()));
        drink = drinkRepository.saveAndFlush(drink);
        return createDto(drink);
    }

    private Map<Ingredient, String> componentsToIngredients(List<DrinkComponent> components) {
        Map<Ingredient, String> ingredients = new HashMap<>();
        for (DrinkComponent component : components) {
            Ingredient i = ingredientRepository.findOne(component.getIngredient().getId());
            ingredients.put(i, component.getValue());
        }
        return ingredients;
    }

    @Transactional(readOnly=true)
    public Optional<DrinkDto> findDrink(long id) {
        Drink drink = drinkRepository.findOne(id);
        if (null == drink) {
            return Optional.empty();
        }
        DrinkDto dto = createDto(drink);
        return Optional.of(dto);
    }

    @Transactional
    public boolean deleteDrink(long id) {
        Drink drink = drinkRepository.findOne(id);
        if (null == drink) {
            return false;
        }
        drinkRepository.delete(drink);
        return true;
    }

    @Transactional
    public DrinkDto modifyDrink(long id, DrinkAdd drink) {
        Drink oldDrink = drinkRepository.findOne(id);
        oldDrink.setName(drink.getName());
        Category category = categoryRepository.findOne(drink.getCategory().getId());
        oldDrink.setCategory(category);
        Glass glass = glassRepository.findOne(drink.getGlass().getId());
        oldDrink.setGlass(glass);
        Map<Ingredient, String> ingredients = componentsToIngredients(drink.getComponents());
        oldDrink.setIngredients(ingredients);
        oldDrink = drinkRepository.saveAndFlush(oldDrink);
        return createDto(oldDrink);
    }

    @Transactional(readOnly=true)
    public boolean drinkExistsCaseInsensitive(DrinkAdd drink) {
        List<Drink> drinks = drinkRepository.findByNameIgnoreCase(drink.getName());
        if (drinks.isEmpty()) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly=true)
    public boolean isIngredientUsedInDrink(Ingredient ing) {
        Predicate predicate = DrinkPredicates.hasIngredient(ing);
        return drinkQueryDslRepository.exists(predicate);
    }

    @Transactional(readOnly=true)
    public List<DrinkDto> findByIngredientQD(Ingredient ing) {
        Predicate predicate = DrinkPredicates.hasIngredient(ing);
        Iterable<Drink> drinks = drinkQueryDslRepository.findAll(predicate);
        List<DrinkDto> list = new ArrayList<>();
        drinks.spliterator().forEachRemaining(d -> list.add(createDto(d)));
        return list;
    }
    
    public boolean isDrinkValid(DrinkAdd drink) {
        if (null == drink || null == drink.getCategory() || null == drink.getGlass() || null == drink.getName()
                || drink.getName().isEmpty() || null == drink.getComponents() || drink.getComponents().isEmpty()) {
            return false;
        }
        return true;
    }

}
