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
        if (!drinkService.isDrinkValid(drink)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (drinkService.drinkExistsCaseInsensitive(drink)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        DrinkDto dto = drinkService.addDrink(drink);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/drinks/" + dto.getId()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/drinks/{id}", method = RequestMethod.GET)
    public ResponseEntity<DrinkDto> fetchDrink(@PathVariable long id) {
        Optional<DrinkDto> drinkDto = drinkService.findDrink(id);
        if (!drinkDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(drinkDto.get(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/drinks/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DrinkDto> deleteDrink(@PathVariable long id) {
        if (! drinkService.findDrink(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        drinkService.deleteDrink(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/drinks/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DrinkDto> modifyDrink(@PathVariable long id, @RequestBody DrinkAdd drink) {
        if (! drinkService.findDrink(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!drinkService.isDrinkValid(drink)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DrinkDto dto = drinkService.modifyDrink(id, drink);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
