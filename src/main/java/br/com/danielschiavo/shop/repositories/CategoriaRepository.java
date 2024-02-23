package br.com.danielschiavo.shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.categoria.Categoria;

public interface CategoriaRepository extends JpaRepository <Categoria, Long> {

	Page<Categoria> findAll(Pageable pageable);
}
