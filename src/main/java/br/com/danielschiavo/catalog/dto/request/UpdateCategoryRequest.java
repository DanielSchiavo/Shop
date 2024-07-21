package br.com.danielschiavo.catalog.dto.request;

public record UpdateCategoryRequest(
        String name,
        String description,
        String image,
        Long parentCategoryId
) {
}
