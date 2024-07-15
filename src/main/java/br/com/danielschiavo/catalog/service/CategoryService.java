package br.com.danielschiavo.produto.service;

import br.com.danielschiavo.produto.model.entity.Category;
import br.com.danielschiavo.produto.repository.CategoryRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional
	public void deleteCategoryById(Long categoryId) {
		Category category = repository.getReferenceById(categoryId);
		repository.delete(category);
	}

	@Transactional
	public Category createCategory(String categoryName) {
		repository.findByNomeLowerCase(categoryName)
				.ifPresent(c -> {throw new ValidationException("A category with name: " + categoryName + " already exist");});

		Category category = new Category(null, categoryName);
		repository.save(category);
		return category;
	}

	@Transactional
	public Category updateCategory(Long categoryId, String name) {
		var category = getCategoryById(categoryId);

		category.setName(name);
		return repository.save(category);
	}

	public Category getCategoryById(Long id){
		return repository.findById(id)
				.orElseThrow(() -> new ValidationException("There are no Category with id: " + id));
	}

	public Page<Category> getAllCategories(Pageable pageable) {
		return repository.findAll(pageable);
	}
	

//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
