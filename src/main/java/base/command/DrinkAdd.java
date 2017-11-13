package base.command;

import java.util.ArrayList;
import java.util.List;

import base.dto.CategoryDto;
import base.dto.DrinkComponent;
import base.dto.GlassDto;

public class DrinkAdd {

    private String name;
    private CategoryDto category;
    private GlassDto glass;
    private List<DrinkComponent> components = new ArrayList<>();

    public DrinkAdd() {
    }

    public DrinkAdd(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public GlassDto getGlass() {
        return glass;
    }

    public void setGlass(GlassDto glass) {
        this.glass = glass;
    }

    public List<DrinkComponent> getComponents() {
        return components;
    }

    public void setComponents(List<DrinkComponent> components) {
        this.components = components;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((components == null) ? 0 : components.hashCode());
        result = prime * result + ((glass == null) ? 0 : glass.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DrinkAdd other = (DrinkAdd) obj;
        if (category == null) {
            if (other.category != null) return false;
        }
        else if (!category.equals(other.category)) return false;
        if (components == null) {
            if (other.components != null) return false;
        }
        else if (!components.equals(other.components)) return false;
        if (glass == null) {
            if (other.glass != null) return false;
        }
        else if (!glass.equals(other.glass)) return false;
        if (name == null) {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }

}
