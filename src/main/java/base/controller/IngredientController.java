package base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.service.IngredientDto;
import base.service.IngredientService;

@RestController
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;
    
    @RequestMapping(value = "/api/ingredients", method = RequestMethod.GET)
    public List<IngredientDto> listAll() {
        return ingredientService.listAll();
    }
}
