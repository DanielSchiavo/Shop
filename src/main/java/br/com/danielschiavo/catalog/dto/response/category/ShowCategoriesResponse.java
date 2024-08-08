package br.com.danielschiavo.catalog.dto.response.category;

import br.com.danielschiavo.shared.DetailFileResponse;
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
public class ShowCategoriesResponse {

    private Long id;
    private String name;
    private String description;
    private DetailFileResponse image;
    private List<ShowCategoriesResponse> children;

    public ShowCategoriesResponse(Long id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    // Getters and setters (omitted for brevity)

    public void addChild(ShowCategoriesResponse child) {
        children.add(child);
    }
}
