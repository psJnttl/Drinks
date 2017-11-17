package base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findByNameIgnoreCase(String name);
}
