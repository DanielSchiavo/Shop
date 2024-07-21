package br.com.danielschiavo.catalog.service;

import br.com.danielschiavo.catalog.dto.request.CreateCategoryRequest;
import br.com.danielschiavo.catalog.dto.request.UpdateCategoryRequest;
import br.com.danielschiavo.catalog.dto.response.CategoryDto;
import br.com.danielschiavo.catalog.mapper.CategoryMapper;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.repository.CategoryRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Autowired
	private CategoryMapper mapper;
	
	@Transactional
	public void deleteCategoryById(Long categoryId) {
		Category category = repository.getReferenceById(categoryId);
		repository.delete(category);
	}

	@Transactional
	public Category createCategory(CreateCategoryRequest request) {
		repository.findByNomeLowerCase(request.name())
				.ifPresent(c -> {throw new ValidationException("A category with name: " + request.name() + " already exist");});

		Category category = mapper.toEntity(request);

		if (request.parentCategoryId() != null) {
			Category parentCategory = getCategoryById(request.parentCategoryId());
			category.setParentCategoryId(parentCategory.getId());
		}

		repository.save(category);
		return category;
	}

	@Transactional
	public Category updateCategory(Long categoryId, UpdateCategoryRequest request) {
		var category = getCategoryById(categoryId);

		mapper.update(category, request);

		if (request.parentCategoryId() != null) {
			Category parentCategory = getCategoryById(request.parentCategoryId());
			category.setParentCategoryId(parentCategory.getId());
		}

		return repository.save(category);
	}

	public Category getCategoryById(Long id){
		return repository.findById(id)
				.orElseThrow(() -> new ValidationException("There are no Category with id: " + id));
	}

	public Page<Category> getAllCategories(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public List<Category> findAllParentCategories() {
		return repository.findAllParentCategories();
	}

	public List<CategoryDto> getASpecificId(Long categoryId) {
		List<Category> all = repository.getCategoryByIdAndAllSubCategories(categoryId);
		return mapToDto(all);
	}

	public List<CategoryDto> findAll() {
		List<Category> all = repository.findAll();

		return mapToDto(all);
	}

	public List<CategoryDto> mapToDto(List<Category> all) {
		Map<Long, CategoryDto> allDtoMap = new HashMap<>();
		List<CategoryDto> rootDtos = new ArrayList<>();

		// Map every from the list to dto
		for (Category category : all) {
			CategoryDto dto = new CategoryDto(category.getId(), category.getName());
			allDtoMap.put(category.getId(), dto);
		}

		// Build hierarchical structure
		for (Category category : all) {
			CategoryDto dto = allDtoMap.get(category.getId());
			Long parentCategoryId = category.getParentCategoryId();

			if (parentCategoryId != null) {
				CategoryDto parentDto = allDtoMap.get(parentCategoryId);
				parentDto.addChild(dto);
			} else {
				rootDtos.add(dto);
			}
		}

		return rootDtos;
	}

}
