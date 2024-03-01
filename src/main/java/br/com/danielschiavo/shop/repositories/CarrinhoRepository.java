package br.com.danielschiavo.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.cliente.Cliente;

@Repository
public interface CarrinhoRepository extends JpaRepository <Carrinho, Long>{

	Optional<Carrinho> findByCliente(Cliente cliente);

	@Query("SELECT c FROM Carrinho c JOIN c.itemsCarrinho items WHERE items.produtoId = :produtoId")
	List<Carrinho> findCarrinhosByProdutoId(@Param("produtoId") Long produtoId);
	
}
