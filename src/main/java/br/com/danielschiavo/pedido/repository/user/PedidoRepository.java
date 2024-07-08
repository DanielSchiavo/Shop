package br.com.danielschiavo.pedido.repository.user;

import java.util.List;
import java.util.UUID;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.model.Pedido;
import br.com.danielschiavo.pedido.model.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PedidoRepository extends JpaRepository<Pedido, UUID>{

	Page<Pedido> findAllByStatusPedido(StatusPedido confirmando, Pageable pageable);

	List<Pedido> findAllByClienteIdOrderByDataPedidoAsc(Long id);

	Page<Pedido> findAllByCliente(Cliente cliente, Pageable pageable);

}
