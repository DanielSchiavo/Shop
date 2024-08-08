package br.com.danielschiavo.catalog.service.category.validators;

import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.repository.CategoryRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateNameCategoryAlreadyExist implements ValidatorCategory {

    @Autowired
    private CategoryRepository repository;

    @Override
    public void validate(Category category) {
        repository.findByNomeLowerCase(category.getName())
                .ifPresent(c -> {throw new ValidationException("A category with name " + c.getName() + " already exists");});
    }
}
