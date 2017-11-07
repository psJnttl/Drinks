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

import base.command.CategoryAdd;
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
    
    @RequestMapping(value = "/api/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryAdd category) throws URISyntaxException {
        Optional<CategoryDto> cDto = categoryService.addCategory(category);
        if (! cDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CategoryDto dto = cDto.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/categories/" + dto.getId()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/api/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDto> findCategorys(@PathVariable long id) {
        Optional<CategoryDto> cDto = categoryService.findCategory(id);
        if (! cDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cDto.get(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable long id) {
        if (! categoryService.deleteCategory(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
