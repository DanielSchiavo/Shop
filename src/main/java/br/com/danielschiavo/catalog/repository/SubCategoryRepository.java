package br.com.danielschiavo.catalog.repository;

import java.util.Optional;

import br.com.danielschiavo.catalog.model.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{
	
	Page<SubCategory> findAll(Pageable pageable);

	@Query("SELECT sc FROM SubCategory sc WHERE LOWER(sc.name) = LOWER(:newName)")
	Optional<SubCategory> findByNomeLowerCase(String newName);
}
