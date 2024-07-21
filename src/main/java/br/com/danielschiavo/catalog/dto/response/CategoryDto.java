package br.com.danielschiavo.catalog.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"children"})
public class CategoryDto {

    private Long id;
    private String name;
    private List<CategoryDto> children;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    // Getters and setters (omitted for brevity)

    public void addChild(CategoryDto child) {
        children.add(child);
    }
}
