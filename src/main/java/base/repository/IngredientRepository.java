package base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);
    List<Ingredient> findByNameIgnoreCase(String name);
}
