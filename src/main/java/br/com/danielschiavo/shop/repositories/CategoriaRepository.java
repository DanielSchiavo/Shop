package br.com.danielschiavo.shop.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.danielschiavo.shop.models.categoria.Categoria;

public interface CategoriaRepository extends JpaRepository <Categoria, Long> {

	Page<Categoria> findAll(Pageable pageable);
	
    @Query("SELECT c FROM Categoria c WHERE c.nome = :novoNome")
    Optional<Categoria> findByNome(String novoNome);
}
