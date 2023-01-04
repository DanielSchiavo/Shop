package br.com.danielschiavo.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.category.Category;

public interface CategoryRepository extends JpaRepository <Category, Long> {

}
