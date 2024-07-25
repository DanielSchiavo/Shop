package br.com.danielschiavo.payment.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.payment.model.enums.PaymentStatus;
import br.com.danielschiavo.payment.model.valueobject.PaymentCard;
import br.com.danielschiavo.payment.model.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "orders_payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method")
	private PaymentMethod paymentMethod;

	@Embedded
	private PaymentCard paymentCard;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status")
	private PaymentStatus paymentStatus;

	private LocalDateTime paymentDateTime;

	private UUID orderId;
	
}
