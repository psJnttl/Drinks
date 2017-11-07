package base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.dto.CategoryDto;
import base.service.CategoryService;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping(value = "/api/categories", method = RequestMethod.GET)
    public List<CategoryDto> listAll() {
        return categoryService.listAll();
    }
}
