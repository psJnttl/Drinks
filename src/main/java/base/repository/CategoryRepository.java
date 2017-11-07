package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
