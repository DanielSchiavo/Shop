package br.com.danielschiavo.shop.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.danielschiavo.shop.models.produto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	Page<Produto> findAll(Pageable pageable);

	Page<Produto> findAllByAtivoTrue(Pageable pageable);

	@Query("SELECT f FROM Produto p JOIN p.arquivosProduto f WHERE p.id = :produtoId AND f.posicao = :posicao")
	Optional<ArquivosProduto> findArquivosProdutoByProdutoIdAndPosicao(@Param("posicao") Integer posicao,
			@Param("produtoId") Long produtoId);

	@Modifying
	@Query("UPDATE Produto p SET p.arquivosProduto = NULL WHERE p.id = :produtoId")
	void deleteArquivosProdutoByProdutoId(@Param("produtoId") Long produtoId);

}