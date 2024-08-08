package br.com.danielschiavo.catalog.controller.user;

import br.com.danielschiavo.catalog.dto.response.category.ShowCategoriesResponse;
import br.com.danielschiavo.catalog.dto.response.category.DetailCategoryResponse;
import br.com.danielschiavo.catalog.service.category.CategoryService;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import br.com.danielschiavo.filestorage.infra.cloud.StorageProperties;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.DetailFileResponse;
import br.com.danielschiavo.shared.FileMapper;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/categories")
@Tag(name = "Category - User", description = "All endpoints related to Categories, for public use")
public class CategoryUserController {
	
	@Autowired
	private CategoryService service;

	@Autowired
	private FileReferenceService fileService;

	@Autowired
	private FileMapper fileMapper;

	@Autowired
	private StorageProperties storageProperties;
	
	@GetMapping("/{categoryName}")
	@Operation(summary = "Get all existing Categories starting with provided name")
	public ResponseEntity<?> getAllCategories(@PathVariable String categoryName){
		List<DetailCategoryResponse> categories = service.getAllCategoriesByName(categoryName);

		List<DetailFileResponse> allCategoriesFiles = categories.stream().map(DetailCategoryResponse::image).toList();
		Set<String> fileNames = categories.stream().map(a -> a.image().getFileName()).collect(Collectors.toSet());

		fileMapper.mapFilesToDto(allCategoriesFiles, CategoryService.awsS3Directory, fileNames);

		return ResponseEntity.ok(Response.success("Success recovering all starting with provided name", categories));
	}

	@GetMapping
	@Operation(summary = "Get all existing categories hierarchicaly")
	public ResponseEntity<?> getAllCategoriesHierarchical(Pageable pageable){
		List<ShowCategoriesResponse> categories = service.getAllRootCategoriesAndItsChildren(pageable);

		List<DetailFileResponse> allCategoriesFiles = categories.stream().map(ShowCategoriesResponse::getImage).toList();
		Set<String> fileNames = categories.stream().map(a -> a.getImage().getFileName()).collect(Collectors.toSet());

		fileMapper.mapFilesToDto(allCategoriesFiles, CategoryService.awsS3Directory, fileNames);

		return ResponseEntity.ok(Response.success("Success recovering all categories", categories));
	}

	@GetMapping("/{categoryId}")
	@Operation(summary = "Get a category by id and their sub categories")
	public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId){
		List<ShowCategoriesResponse> allDto = service.getRootCategoryByIdAndItsChildren(categoryId);
		return ResponseEntity.ok(Response.success("Success recovering all categories", allDto));
	}
}
