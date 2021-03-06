package base.dto;

import java.util.ArrayList;
import java.util.List;

public class DrinkDto {

    private Long id;
    private String name;
    private CategoryDto category;
    private GlassDto glass;
    private List<DrinkComponent> components = new ArrayList<>();

    public DrinkDto() {
    }

    public DrinkDto(Long id, String name, CategoryDto category, GlassDto glass, List<DrinkComponent> components) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.glass = glass;
        this.components = components;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DrinkDto other = (DrinkDto) obj;
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
        if (id == null) {
            if (other.id != null) return false;
        }
        else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }
    
    
}
