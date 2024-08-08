package br.com.danielschiavo.catalog.dto.response.category;

import br.com.danielschiavo.shared.DetailFileResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailCategoryResponse(
        Long id,
        String name,
        String description,
        DetailFileResponse image,
        @JsonProperty("parent_category_id")
        Long parentCategoryId

) {
}
