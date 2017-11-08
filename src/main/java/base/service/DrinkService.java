package base.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import base.domain.Drink;
import base.domain.Glass;
import base.domain.Ingredient;
import base.repository.DrinkRepository;

@Service
public class DrinkService {

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Drink> findByIngredient(Ingredient ing) {
        List<Long> list = jdbcTemplate.query(
                "SELECT D.id FROM drink D, ingredient I, drink_ingredients DI "
                        + "WHERE I.id=? AND D.id=DI.drink_id AND I.id = DI.ingredient_id",
                new Object[] { ing.getId(), }, (rs, rowNbr) -> resultExtractor(rs));
        return list.stream().map(i -> drinkRepository.findOne(i)).collect(Collectors.toList());
    }

    private Long resultExtractor(ResultSet rs) throws SQLException {
        return rs.getLong("id");
    }

    public List<Drink> findByGlass(Glass glass) {
        return drinkRepository.findByGlass(glass);
    }

}
