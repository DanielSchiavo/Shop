package br.com.danielschiavo.shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	Page<Product> findAllByActiveTrue(Pageable pageable);

}
