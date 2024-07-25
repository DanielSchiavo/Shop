package br.com.danielschiavo.payment.mapper;

import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;
import br.com.danielschiavo.payment.dto.response.ShowPaymentResponse;
import br.com.danielschiavo.payment.model.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toEntity(AddPaymentRequest request);

    ShowPaymentResponse toDto(Payment payment);
}
