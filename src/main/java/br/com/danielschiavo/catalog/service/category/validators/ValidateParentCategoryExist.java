package br.com.danielschiavo.catalog.service.category.validators;

import br.com.danielschiavo.catalog.exception.CategoryNotFoundException;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateParentCategoryExist implements ValidatorCategory {

    @Autowired
    private CategoryRepository repository;

    @Override
    public void validate(Category category) {
        Long parentCategoryId = category.getParentCategoryId();
        if (parentCategoryId != null && !repository.existsById(parentCategoryId)) {
            throw new CategoryNotFoundException("Category with ID " + parentCategoryId + " does not exist.");
        }
    }
}
