package br.com.danielschiavo.catalog.service.category;

import br.com.danielschiavo.catalog.dto.request.category.CreateCategoryRequest;
import br.com.danielschiavo.catalog.dto.request.category.UpdateCategoryRequest;
import br.com.danielschiavo.catalog.dto.response.category.ShowCategoriesResponse;
import br.com.danielschiavo.catalog.dto.response.category.DetailCategoryResponse;
import br.com.danielschiavo.catalog.mapper.CategoryMapper;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.repository.CategoryRepository;
import br.com.danielschiavo.catalog.service.category.validators.ValidatorCategory;
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

	@Autowired
	private List<ValidatorCategory> validators;

	public static final String awsS3Directory = "categories";

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
		Category category = mapper.toEntity(request);

		validators.forEach(v -> v.validate(category));

		return mapper.toDetailCategory(repository.save(category));
	}

	@Transactional
	public DetailCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
		Category category = repository.findById(categoryId)
				.orElseThrow(() -> new ValidationException("Cannot update because there is no category with given id: " + categoryId));
		mapper.update(category, request);

		validators.forEach(v -> v.validate(category));

		return mapper.toDetailCategory(repository.save(category));
	}

	public DetailCategoryResponse getCategoryById(Long id){
		Category category = repository.findById(id)
				.orElseThrow(() -> new ValidationException("There is no Category with id: " + id));
		return mapper.toDetailCategory(category);
	}

	public List<ShowCategoriesResponse> getRootCategoryByIdAndItsChildren(Long categoryId) {
		List<Category> all = repository.getCategoryByIdAndAllSubCategories(categoryId);
		return mapToDto(all);
	}

	public List<ShowCategoriesResponse> getAllRootCategoriesAndItsChildren(Pageable pageable) {
		Page<Category> all = repository.findAll(pageable);
		return mapToDto(all.getContent());
	}

	public List<DetailCategoryResponse> getAllCategoriesByName(String categoryName) {
		List<Category> all = repository.findByNameStartingWith(categoryName)
				.orElseThrow(() -> new ValidationException("There's no category starting with " + categoryName));

		return all.stream().map(mapper::toDetailCategory).toList();
	}

	public List<ShowCategoriesResponse> mapToDto(List<Category> all) {
		Map<Long, ShowCategoriesResponse> allDtoMap = new HashMap<>();
		List<ShowCategoriesResponse> rootDtos = new ArrayList<>();

		// Map every from the list to dto
		for (Category category : all) {
			ShowCategoriesResponse dto = new ShowCategoriesResponse(category.getId(), category.getName());
			allDtoMap.put(category.getId(), dto);
		}

		// Build hierarchical structure
		for (Category category : all) {
			ShowCategoriesResponse dto = allDtoMap.get(category.getId());
			Long parentCategoryId = category.getParentCategoryId();

			if (parentCategoryId != null) {
				ShowCategoriesResponse parentDto = allDtoMap.get(parentCategoryId);
				parentDto.addChild(dto);
			} else {
				rootDtos.add(dto);
			}
		}

		return rootDtos;
	}

	public void categoryExists(Long categoryId) {
		if (!repository.existsById(categoryId)) {
			throw new ValidationException("There is no category with provided id");
		}
	}
}
