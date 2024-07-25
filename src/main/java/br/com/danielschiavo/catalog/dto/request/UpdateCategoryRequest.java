package br.com.danielschiavo.catalog.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryRequest(
        String name,
        String description,
        String image,
        @JsonProperty("parent_category_id")
        Long parentCategoryId
) {
}
