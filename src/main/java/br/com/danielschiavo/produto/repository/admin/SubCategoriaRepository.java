package br.com.danielschiavo.produto.repository.admin;

import java.util.Optional;

import br.com.danielschiavo.produto.model.categoria.subcategoria.SubCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long>{
	
	Page<SubCategoria> findAll(Pageable pageable);

	Optional<SubCategoria> findByNome(String nome);
}
