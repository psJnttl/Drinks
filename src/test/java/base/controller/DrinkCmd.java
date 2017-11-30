package base.controller;

import java.util.ArrayList;
import java.util.List;

import base.dto.CategoryDto;
import base.dto.DrinkComponent;
import base.dto.GlassDto;

public class DrinkCmd {

    private String name;
    private CategoryDto category;
    private GlassDto glass;
    private List<DrinkComponent> components = new ArrayList<>();

    private DrinkCmd(Builder builder) {
        this.name = builder.name;
        this.category = builder.category;
        this.glass = builder.glass;
        this.components = builder.components;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private CategoryDto category;
        private GlassDto glass;
        private List<DrinkComponent> components = new ArrayList<>();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(CategoryDto category) {
            this.category = category;
            return this;
        }
        
        public Builder glass(GlassDto glass) {
            this.glass = glass;
            return this;
        }

        public Builder components(List<DrinkComponent> components) {
            this.components = components;
            return this;
        }
        
        public Builder addComponent(DrinkComponent component) {
            this.components.add(component);
            return this;
        }

        public DrinkCmd build() {
            return new DrinkCmd(this);
        }
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


    
    
}
