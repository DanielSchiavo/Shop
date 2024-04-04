package br.com.danielschiavo.shop.repositories.cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.carrinho.Carrinho;

@Repository
public interface CarrinhoRepository extends JpaRepository <Carrinho, Long>{

	Optional<Carrinho> findByCliente(Cliente cliente);

	@Query("SELECT c FROM Carrinho c JOIN c.itemsCarrinho items WHERE items.produto.id = :produtoId")
	Optional<List<Carrinho>> findCarrinhosByProdutoId(@Param("produtoId") Long produtoId);

//Postgres NativeQuery	@Query(value = "SELECT * FROM carrinhos c JOIN carrinhos_items ci ON c.id = ci.carrinho_id WHERE c.cliente_id = :id", nativeQuery = true)
//JPQL 	@Query("SELECT c FROM Carrinho c JOIN c.itemsCarrinho ic ON c.id = ic.id WHERE c.id = :id")
    Carrinho findByItemsCarrinhoCarrinhoId(Long carrinhoId);

    @Modifying
    @Query("DELETE FROM ItemCarrinho ic WHERE ic.produto.id IN :ids AND ic.carrinho.id = :carrinhoId")
	int deletarItemsCarrinhoPorListaDeIds(List<Long> ids, Long carrinhoId);
	
}
