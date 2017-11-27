package base.domain;

import com.querydsl.core.types.Predicate;

public final class DrinkPredicates {

    private DrinkPredicates() {}
    
    /**
     * Test if Drink's ingredients map contains key.
     * @param ingredient  key to test.
     * @return
     */
    public static Predicate hasIngredient(Ingredient ingredient) {
        return QDrink.drink.ingredients.containsKey(ingredient);
    }
}
