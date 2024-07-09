package br.com.danielschiavo.pedido.repository;

import java.util.List;
import java.util.UUID;

import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.model.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PedidoRepository extends JpaRepository<Pedido, UUID>{

	Page<Pedido> findAllByStatusPedido(StatusPedido confirmando, Pageable pageable);

	List<Pedido> findAllByClienteIdOrderByDataPedidoAsc(Long id);

	Page<Pedido> findAllByClienteId(Pageable pageable, Long clienteId);

}
