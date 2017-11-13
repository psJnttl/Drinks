package base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
