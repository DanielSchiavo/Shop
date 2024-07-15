package br.com.danielschiavo.pedido.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.danielschiavo.pedido.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, UUID>{

	Page<Order> findAllByCustomerId(Pageable pageable, Long clienteId);

	Optional<Order> findByIdAndCustomerId(UUID pedidoId, Long clienteId);
}
