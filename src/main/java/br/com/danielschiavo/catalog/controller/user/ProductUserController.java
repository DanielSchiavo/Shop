package br.com.danielschiavo.catalog.controller.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.danielschiavo.catalog.dto.response.product.DetailProductResponse;
import br.com.danielschiavo.catalog.dto.response.product.DetailProductFileResponse;
import br.com.danielschiavo.catalog.dto.response.product.ShowProductsResponse;
import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import br.com.danielschiavo.filestorage.infra.cloud.StorageProperties;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.DetailFileResponse;
import br.com.danielschiavo.shared.FileMapper;
import br.com.danielschiavo.shared.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public/products")
@Tag(name = "Product - User", description = "All endpoints related to a Product, for public use")
public class ProductUserController {

	@Autowired
	private ProductService service;

	@Autowired
	private FileMapper fileMapper;

	@GetMapping
	@Operation(summary = "Get all products")
	public ResponseEntity<?> getAllProducts(Pageable pageable) {
		List<ShowProductsResponse> products = service.getAllProducts(pageable);

		Set<String> firstImagesNames = products.stream().map(p -> p.getFirstImage().getFileData().getFileName()).collect(Collectors.toSet());

		List<DetailFileResponse> allProductsFiles = products.stream().map(p -> p.getFirstImage().getFileData()).toList();

		fileMapper.mapFilesToDto(allProductsFiles, ProductService.awsS3Directory, firstImagesNames);

		return ResponseEntity.ok(Response.success("Success recovering all products", new PageImpl<>(products, pageable, products.size())));
	}
	
	@GetMapping("/{productId}")
	@Operation(summary = "Get all product data by id")
	public ResponseEntity<?> detailProductById(@PathVariable Long productId) {
		DetailProductResponse product = service.getProductById(productId);

		List<DetailFileResponse> allProductsFiles = product.getFiles().stream().map(DetailProductFileResponse::getFileData).toList();
		Set<String> fileNames = product.getFiles().stream().filter(pf -> pf.getType().equals(ProductFileType.IMAGE))
				.map(p -> p.getFileData().getFileName()).collect(Collectors.toSet());

		fileMapper.mapFilesToDto(allProductsFiles, ProductService.awsS3Directory, fileNames);

		return ResponseEntity.ok(Response.success("Success recovering all product data", product));
	}

}
