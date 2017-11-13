package base.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import base.domain.Category;
import base.domain.Drink;
import base.domain.Glass;
import base.domain.Ingredient;
import base.dto.CategoryDto;
import base.dto.DrinkComponent;
import base.dto.DrinkDto;
import base.dto.GlassDto;
import base.dto.IngredientDto;
import base.repository.DrinkRepository;
import base.repository.CategoryRepository;
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

    public List<Drink> findByGlass(Glass glass) {
        return drinkRepository.findByGlass(glass);
    }

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

    public List<Category> findByCategory(Category cat) {
        return drinkRepository.findByCategory(cat);
    }

}
