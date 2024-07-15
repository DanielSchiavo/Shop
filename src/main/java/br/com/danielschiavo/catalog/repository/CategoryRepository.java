package br.com.danielschiavo.catalog.repository;

import java.util.Optional;

import br.com.danielschiavo.catalog.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CategoryRepository extends JpaRepository <Category, Long> {

	Page<Category> findAll(Pageable pageable);
	
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:newName)")
    Optional<Category> findByNomeLowerCase(String newName);

}
