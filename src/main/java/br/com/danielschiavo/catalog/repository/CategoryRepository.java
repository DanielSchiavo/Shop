package br.com.danielschiavo.catalog.repository;

import java.util.List;
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

    @Query("SELECT c FROM Category c WHERE c.parentCategoryId IS NULL")
    List<Category> findAllParentCategories();

    @Query(value = """
        WITH RECURSIVE CategoryHierarchy AS (
        SELECT id, name, description, image, parent_category_id FROM categories
            WHERE id = :id
        
        UNION ALL
        
        SELECT c.id, c.name, c.description, c.image, c.parent_category_id FROM categories c
            INNER JOIN CategoryHierarchy ch ON c.parent_category_id = ch.id)
        
        SELECT * FROM CategoryHierarchy;""",
            nativeQuery = true)
    List<Category> getCategoryByIdAndAllSubCategories(Long id);

    Optional<List<Category>> findByNameStartingWith(String categoryName);
}
