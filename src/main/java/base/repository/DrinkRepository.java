package base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Category;
import base.domain.Drink;
import base.domain.Glass;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
    Drink findByName(String name);
    List<Drink> findByCategory(Category category);
    List<Drink> findByGlass(Glass glass);
}
