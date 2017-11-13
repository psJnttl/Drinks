package base.dto;

public class DrinkComponent {
    private IngredientDto ingredient;
    private String value;

    public DrinkComponent() {
    }

    public DrinkComponent(IngredientDto ingredient, String value) {
        this.ingredient = ingredient;
        this.value = value;
    }

    public IngredientDto getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientDto ingredient) {
        this.ingredient = ingredient;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ingredient == null) ? 0 : ingredient.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DrinkComponent other = (DrinkComponent) obj;
        if (ingredient == null) {
            if (other.ingredient != null) return false;
        }
        else if (!ingredient.equals(other.ingredient)) return false;
        if (value == null) {
            if (other.value != null) return false;
        }
        else if (!value.equals(other.value)) return false;
        return true;
    }

}
