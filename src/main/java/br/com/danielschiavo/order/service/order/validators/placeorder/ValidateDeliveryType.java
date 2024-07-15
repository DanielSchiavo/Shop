package br.com.danielschiavo.order.service.order.validators.placeorder;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.order.model.enums.DeliveryType;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.stereotype.Service;


@Service
public class ValidateDeliveryType implements ValidatorPlaceOrder {

	@Override
	public void validate(Order order, Customer customer) {
		DeliveryType deliveryType = order.getDelivery().getDeliveryType();

		if (deliveryType.needsAddress()) {
			if (order.getDelivery().getOrderAddress() == null || order.getDelivery().getOrderAddress().getAddressId() == null) {
				throw new ValidationException("The delivery type defined was " + deliveryType + ", therefore, it is required to provide the delivery address.");
			}
		} else {
			if (order.getDelivery().getOrderAddress() != null && order.getDelivery().getOrderAddress().getAddressId() != null) {
				throw new ValidationException("The delivery type defined was " + deliveryType + ", therefore, it is not required to provide the enderecoId.");
			}
		}
	}

}
