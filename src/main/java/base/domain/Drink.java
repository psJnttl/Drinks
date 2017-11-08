package base.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Drink extends AbstractPersistable<Long> {
    private String name;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Glass glass;

    @ElementCollection
    @CollectionTable(name = "drink_ingredients")
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "amount")
    private Map<Ingredient, String> ingredients = new HashMap<>();

    public Drink() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Glass getGlass() {
        return glass;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public void addIngredient(Ingredient key, String value) {
        ingredients.put(key, value);
    }

    public Map<Ingredient, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, String> ingredients) {
        this.ingredients = ingredients;
    }

}
