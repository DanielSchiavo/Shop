package br.com.danielschiavo.catalog.mapper;

import br.com.danielschiavo.catalog.dto.request.CreateCategoryRequest;
import br.com.danielschiavo.catalog.dto.request.UpdateCategoryRequest;
import br.com.danielschiavo.catalog.model.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Category toEntity(CreateCategoryRequest request);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(@MappingTarget Category category, UpdateCategoryRequest request);
}
