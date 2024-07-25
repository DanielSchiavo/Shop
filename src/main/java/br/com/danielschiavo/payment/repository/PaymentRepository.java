package br.com.danielschiavo.payment.repository;

import br.com.danielschiavo.payment.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
