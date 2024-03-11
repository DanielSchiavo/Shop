package br.com.danielschiavo.shop.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long>{
	
	Page<SubCategoria> findAll(Pageable pageable);

    @Query("SELECT sc FROM SubCategoria sc WHERE sc.nome = :novoNome")
	Optional<SubCategoria> findByNome(String novoNome);

}
