package base.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    @RequestMapping(value = "/api/ingredients", method = RequestMethod.POST)
    public ResponseEntity<IngredientDto> addIngredient(@RequestBody IngredientAdd ingredient) throws URISyntaxException {
        Optional<IngredientDto> iDto = ingredientService.addIngredient(ingredient);
        if (!iDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        IngredientDto dto = iDto.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/ingredients/" + dto.getId()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/api/ingredients/{id}", method = RequestMethod.GET)
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable long id) {
        Optional<IngredientDto> iDto  = ingredientService.findIngredient(id);
        if (! iDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(iDto.get(), HttpStatus.OK);
    }
}
