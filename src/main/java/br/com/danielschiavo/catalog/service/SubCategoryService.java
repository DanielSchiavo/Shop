package br.com.danielschiavo.produto.service;

import br.com.danielschiavo.produto.model.entity.Category;
import br.com.danielschiavo.produto.model.entity.SubCategory;
import br.com.danielschiavo.produto.repository.SubCategoryRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubCategoryService {

	@Autowired
	private SubCategoryRepository repository;

	@Autowired
	private CategoryService categoryService;
	
	@Transactional
	public void deleteSubCategoryById(Long subCategoryId) {
		SubCategory subCategory = repository.getReferenceById(subCategoryId);
		repository.delete(subCategory);
	}
	
	@Transactional
	public SubCategory createSubCategory(String name, Long categoryId) {
		Category category = categoryService.getCategoryById(categoryId);

		repository.findByNomeLowerCase(name).ifPresent(sc -> {throw new ValidationException("A Sub Category with name: " + name + " already exist");});

		SubCategory subCategory = new SubCategory(null, name, category.getId());
		return repository.save(subCategory);
	}
	
	@Transactional
	public SubCategory updateSubCategory(Long subCategoryId, String name, Long categoryId) {
		var subCategoria = getSubCategoryById(subCategoryId);

		if (categoryId != null) {
			Category category = categoryService.getCategoryById(categoryId);
			subCategoria.setCategoryId(category.getId());
		}

		if (name != null) {
			subCategoria.setName(name);
		}

		return repository.save(subCategoria);
	}

	public SubCategory getSubCategoryById(Long subCategoryId){
		return repository.findById(subCategoryId)
				.orElseThrow(() -> new ValidationException("There's no Sub Category with id: " + subCategoryId));
	}

	public Page<SubCategory> getAllSubCategories(Pageable pageable) {
		return repository.findAll(pageable);
	}

}
