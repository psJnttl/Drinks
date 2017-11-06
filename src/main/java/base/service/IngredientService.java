package base.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
