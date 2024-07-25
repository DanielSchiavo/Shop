package br.com.danielschiavo.catalog.service;

import br.com.danielschiavo.catalog.dto.request.CreateCategoryRequest;
import br.com.danielschiavo.catalog.dto.request.UpdateCategoryRequest;
import br.com.danielschiavo.catalog.dto.response.CategoryDto;
import br.com.danielschiavo.catalog.dto.response.DetailCategoryResponse;
import br.com.danielschiavo.catalog.exception.CategoryNotFoundException;
import br.com.danielschiavo.catalog.mapper.CategoryMapper;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.repository.CategoryRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Autowired
	private CategoryMapper mapper;

	@Transactional
	public void deleteCategoryById(Long categoryId) {
		if (!repository.existsById(categoryId)) {
			throw new ValidationException("Unable to delete category with id " + categoryId + " because it does not exist");
		}
		Category category = repository.getReferenceById(categoryId);
		repository.delete(category);
	}

	@Transactional
	public DetailCategoryResponse createCategory(CreateCategoryRequest request) {
		repository.findByNomeLowerCase(request.name())
				.ifPresent(c -> {throw new ValidationException("A category with name " + c.getName() + " already exists");});

		Category createCategory = mapper.toEntity(request);

		return mapper.toDetailCategory(repository.save(createCategory));
	}

	@Transactional
	public DetailCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
		Category category = repository.findById(categoryId)
				.orElseThrow(() -> new ValidationException("Cannot update because there is no category with given id: " + categoryId));
		mapper.update(category, request);

		return mapper.toDetailCategory(repository.save(category));
	}

	public DetailCategoryResponse getCategoryById(Long id){
		Category category = repository.findById(id)
				.orElseThrow(() -> new ValidationException("There is no Category with id: " + id));
		return mapper.toDetailCategory(category);
	}

	public List<CategoryDto> getRootCategoryByIdAndItsChildren(Long categoryId) {
		List<Category> all = repository.getCategoryByIdAndAllSubCategories(categoryId);
		return mapToDto(all);
	}

	public List<CategoryDto> getAllRootCategoriesAndItsChildren() {
		List<Category> all = repository.findAll();
		return mapToDto(all);
	}

	public List<DetailCategoryResponse> getAllCategoriesByName(String categoryName) {
		List<Category> all = repository.findByNameStartingWith(categoryName)
				.orElseThrow(() -> new ValidationException("There's no category starting with " + categoryName));

		return all.stream().map(mapper::toDetailCategory).toList();
	}

	public void validateCategoryExists(Long categoryId) {
		if (categoryId != null && !repository.existsById(categoryId)) {
			throw new CategoryNotFoundException("Category with ID " + categoryId + " does not exist.");
		}
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
