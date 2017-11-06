package base.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.controller.IngredientAdd;
import base.domain.Ingredient;
import base.repository.IngredientRepository;

@Service
public class IngredientService {

    @Autowired 
    private IngredientRepository ingredientRepository;
    
    public List<IngredientDto> listAll() {
        List<Ingredient> list = ingredientRepository.findAll();
        return list.stream()
                .map(i -> new IngredientDto(i.getId(), i.getName()))
                .collect(Collectors.toList());
    }

    public Optional<IngredientDto> addIngredient(IngredientAdd ingredient) {
        if (null == ingredient || ingredient.getName().isEmpty()) {
            return Optional.empty();
        }
        Ingredient ing = new Ingredient(ingredient.getName());
        ing = ingredientRepository.saveAndFlush(ing);
        IngredientDto dto = new IngredientDto(ing.getId(), ing.getName());
        return Optional.of(dto);
    }

    public Optional<IngredientDto> findIngredient(long id) {
        Ingredient ingredient = ingredientRepository.findOne(id);
        if (null == ingredient) {
            return Optional.empty();
        }
        IngredientDto dto = new IngredientDto(ingredient.getId(), ingredient.getName());
        return Optional.of(dto);
    }
}
