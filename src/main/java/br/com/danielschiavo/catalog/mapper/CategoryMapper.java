package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.request.category.CreateCategoryRequest;
import br.com.danielschiavo.catalog.dto.request.category.UpdateCategoryRequest;
import br.com.danielschiavo.catalog.dto.response.category.DetailCategoryResponse;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Category toEntity(CreateCategoryRequest request);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(@MappingTarget Category category, UpdateCategoryRequest request);

    @Mapping(target = "image.fileName", source = "image")
    DetailCategoryResponse toDetailCategory(Category category);
}
