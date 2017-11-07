package base.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
