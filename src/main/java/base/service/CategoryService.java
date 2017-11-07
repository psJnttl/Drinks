package base.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.command.CategoryAdd;
import base.domain.Category;
import base.dto.CategoryDto;
import base.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> listAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(i -> new CategoryDto(i.getId(), i.getName())).collect(Collectors.toList());
    }

    @Transactional
    public Optional<CategoryDto> addCategory(CategoryAdd category) {
        if (null == category || null == category.getName() || category.getName().isEmpty()) {
            return Optional.empty();
        }
        Category cat = new Category(category.getName());
        cat = categoryRepository.save(cat);
        CategoryDto dto = new CategoryDto(cat.getId(), cat.getName());
        return Optional.of(dto);
    }
}
