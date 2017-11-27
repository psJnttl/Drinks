package base.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.command.IngredientAdd;
import base.domain.Drink;
import base.domain.Ingredient;
import base.dto.IngredientDto;
import base.repository.DrinkQueryDslRepository;
import base.repository.IngredientRepository;

@Service
public class IngredientService {

    @Autowired 
    private IngredientRepository ingredientRepository;
    
    @Autowired
    private DrinkService drinkService; 
    
    public List<IngredientDto> listAll() {
        List<Ingredient> list = ingredientRepository.findAll();
        return list.stream()
                .map(i -> new IngredientDto(i.getId(), i.getName()))
                .collect(Collectors.toList());
    }

    public boolean isIngredientValid(IngredientAdd ingredient) {
        if (null == ingredient || null == ingredient.getName() || ingredient.getName().isEmpty()) {
            return false;
        }
        return true;
    }

    public IngredientDto addIngredient(IngredientAdd ingredient) {
        Ingredient ing = new Ingredient(ingredient.getName());
        ing = ingredientRepository.saveAndFlush(ing);
        IngredientDto dto = new IngredientDto(ing.getId(), ing.getName());
        return dto;
    }

    public Optional<IngredientDto> findIngredient(long id) {
        Ingredient ingredient = ingredientRepository.findOne(id);
        if (null == ingredient) {
            return Optional.empty();
        }
        IngredientDto dto = new IngredientDto(ingredient.getId(), ingredient.getName());
        return Optional.of(dto);
    }

    public boolean deleteIngredient(long id) {
        Ingredient ingredient = ingredientRepository.findOne(id);
        if (null == ingredient) {
            return false;
        }
        ingredientRepository.delete(id);
        return true;
    }

    public IngredientDto modifyIngredient(long id, IngredientAdd ingredient) {
        Ingredient ing = ingredientRepository.findOne(id);
        ing.setName(ingredient.getName());
        ing = ingredientRepository.saveAndFlush(ing);
        IngredientDto dto = new IngredientDto(ing.getId(), ing.getName());
        return dto;
    }
    
    public boolean isIngredientUsed(long id) {
        Ingredient ing = ingredientRepository.findOne(id);
        if (null == ing) {
            return false;
        }
        return drinkService.isIngredientUsedInDrink(ing);
    }

    public boolean ingredientExistsCaseInsensitive(IngredientAdd ingredient) {
        List<Ingredient> ingredients = ingredientRepository.findByNameIgnoreCase(ingredient.getName());
        if (ingredients.isEmpty()) {
            return false;
        }
        return true;
    }

}
