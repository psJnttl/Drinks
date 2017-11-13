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
}
