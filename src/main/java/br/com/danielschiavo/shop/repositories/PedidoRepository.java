package br.com.danielschiavo.shop.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

	Page<Pedido> findAllByStatusPedido(StatusPedido confirmando, Pageable pageable);

	List<Pedido> findAllByClienteIdOrderByDataPedidoAsc(Long id);

}
