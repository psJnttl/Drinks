package base.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.command.DrinkAdd;
import base.dto.DrinkDto;
import base.service.DrinkService;


@RestController
public class DrinkController {

    @Autowired
    private DrinkService drinkService;

    @RequestMapping(value = "/api/drinks", method = RequestMethod.GET)
    public List<DrinkDto> listAll() {
        return drinkService.listAll();
    }
    
    @RequestMapping(value = "/api/drinks", method = RequestMethod.POST)
    public ResponseEntity<DrinkDto> addDrink(@RequestBody DrinkAdd drink) throws URISyntaxException {
        if (null == drink || null == drink.getCategory() || null == drink.getClass() || drink.getName().isEmpty()
                || null == drink.getComponents() || drink.getComponents().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DrinkDto dto = drinkService.addDrink(drink);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/drinks/" + dto.getId()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
}
