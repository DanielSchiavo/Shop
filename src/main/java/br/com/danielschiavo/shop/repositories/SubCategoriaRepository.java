package br.com.danielschiavo.shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long>{
	
	Page<SubCategoria> findAll(Pageable pageable);

}
