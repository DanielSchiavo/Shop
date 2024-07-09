package br.com.danielschiavo.vendas.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.vendas.model.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CarrinhoRepository extends JpaRepository <Carrinho, Long>{

	Optional<Carrinho> findByCliente(Cliente cliente);

	@Query("SELECT c FROM Carrinho c JOIN c.itemsCarrinho items WHERE items.produtoId = :produtoId")
	Optional<List<Carrinho>> findCarrinhosByProdutoId(@Param("produtoId") Long produtoId);

	Optional<Carrinho> findByClienteId(Long clienteId);
}
