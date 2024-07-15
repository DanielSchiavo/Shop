package br.com.danielschiavo.catalog.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findAll(Pageable pageable);

	Page<Product> findAllByAtivoTrue(Pageable pageable);

	@Query("SELECT f FROM Product p JOIN p.productFiles f WHERE p.id = :productId AND f.position = :position")
	Optional<ProductFile> findArquivosProdutoByProdutoIdAndPosicao(@Param("position") Integer position,
																   @Param("productId") Long productId);

	@Modifying
	@Query("UPDATE Product p SET p.productFiles = NULL WHERE p.id = :productId")
	void deleteArquivosProdutoByProdutoId(@Param("productId") Long productId);

	@Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.active = true")
	List<Product> findAllByIdAndAtivoTrue(@Param("ids") List<Long> ids);
	
	Optional<Product> findByIdAndAtivoTrue(Long id);

	@Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
	Optional<Product> findByNomeLowerCase(String name);

	List<Product> findAllByIdIn(List<Long> productsId);

}
