package br.com.danielschiavo.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailCategoryResponse(
        Long id,
        String name,
        String description,
        String image,
        @JsonProperty("parent_category_id")
        Long parentCategoryId

) {
}
