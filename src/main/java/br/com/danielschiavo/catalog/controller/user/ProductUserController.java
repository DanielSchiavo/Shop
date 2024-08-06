package br.com.danielschiavo.catalog.controller.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.danielschiavo.catalog.dto.response.DetailProductResponse;
import br.com.danielschiavo.catalog.dto.response.DetailProductFileResponse;
import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.mapper.ProductMapper;
import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.filestorage.dto.response.DetailFileReferenceResponse;
import br.com.danielschiavo.filestorage.infra.cloud.StorageProperties;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
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
	private FileReferenceService fileService;

	@Autowired
	private ProductMapper mapper;

	@Autowired
	private StorageProperties storageProperties;

	@GetMapping
	@Operation(summary = "Get all products")
	public ResponseEntity<?> getAllProducts(Pageable pageable) {
		List<ShowProductsResponse> products = service.getAllProducts(pageable);

		Set<String> firstImagesNames = products.stream().map(p -> p.getFirstImage().getFileName()).collect(Collectors.toSet());

		List<DetailProductFileResponse> detailProductFileResponseList = products.stream().map(ShowProductsResponse::getFirstImage).toList();
		List<DetailFileReferenceResponse> filesReferences = fileService.getAllById(ProductService.awsS3Directory, firstImagesNames);

		String downloadUrl = storageProperties.getImage().getDownloadUrl().toString();
		mapper.mapFilesToDto(detailProductFileResponseList, filesReferences, downloadUrl);

		var pageProducts = new PageImpl<>(products, pageable, products.size());

		return ResponseEntity.ok(Response.success("Success recovering all products", pageProducts));
	}
	
	@GetMapping("/{productId}")
	@Operation(summary = "Get all product data by id")
	public ResponseEntity<?> detailProductById(@PathVariable Long productId) {
		DetailProductResponse product = service.getProductById(productId);

		List<DetailFileReferenceResponse> filesReferences = fileService.getAllById(
				ProductService.awsS3Directory,
				product.getFiles().stream().filter(pf -> pf.getType().equals(ProductFileType.IMAGE))
						.map(DetailProductFileResponse::getFileName).toList()
		);

		String downloadUrl = storageProperties.getImage().getDownloadUrl().toString();
		mapper.mapFilesToDto(product.getFiles(), filesReferences, downloadUrl);

		return ResponseEntity.ok(Response.success("Success recovering all product data", product));
	}

}
