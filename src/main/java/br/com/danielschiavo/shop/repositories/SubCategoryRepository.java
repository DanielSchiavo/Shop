package br.com.danielschiavo.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.subcategory.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{

}
