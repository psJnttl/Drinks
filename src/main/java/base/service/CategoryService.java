package base.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.command.CategoryAdd;
import base.domain.Category;
import base.domain.Drink;
import base.dto.CategoryDto;
import base.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private DrinkService drinkService;

    @Transactional(readOnly=true)
    public List<CategoryDto> listAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(i -> new CategoryDto(i.getId(), i.getName())).collect(Collectors.toList());
    }

    public boolean isCategoryValid(CategoryAdd category) {
        if (null == category || null == category.getName() || category.getName().isEmpty()) {
            return false;
        }
        return true;
    }

    @Transactional
    public CategoryDto addCategory(CategoryAdd category) {
        Category cat = new Category(category.getName());
        cat = categoryRepository.save(cat);
        CategoryDto dto = new CategoryDto(cat.getId(), cat.getName());
        return dto;
    }

    public Optional<CategoryDto> findCategory(long id) {
        Category cat = categoryRepository.findOne(id);
        if (null == cat) {
            return Optional.empty();
        }
        CategoryDto dto = new CategoryDto(cat.getId(), cat.getName());
        return Optional.of(dto);
    }

    @Transactional
    public boolean deleteCategory(long id) {
        Category category = categoryRepository.findOne(id);
        if (null == category) {
            return false;
        }
        categoryRepository.delete(category);
        return true;
    }

    @Transactional
    public CategoryDto modifyCategory(long id, CategoryAdd category) {
        Category cat = categoryRepository.findOne(id);
        cat.setName(category.getName());
        cat = categoryRepository.save(cat);
        CategoryDto dto = new CategoryDto(cat.getId(), cat.getName());
        return dto;
    }
    
    @Transactional(readOnly=true)
    public boolean isCategoryUsed(long id) {
        Category cat = categoryRepository.findOne(id);
        if (null == cat) {
            return false;
        }
        List<Drink> list = drinkService.findByCategory(cat);
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }
    
    @Transactional(readOnly=true)
    public boolean categoryExistsCaseInsensitive(CategoryAdd category) {
        String nameToTest = category.getName();
        List<Category> categories = categoryRepository.findByNameIgnoreCase(nameToTest);
        if (categories.isEmpty()) {
            return false;
        }
        return true;
    }
}
