package br.com.danielschiavo.delivery.repository;

import br.com.danielschiavo.delivery.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
