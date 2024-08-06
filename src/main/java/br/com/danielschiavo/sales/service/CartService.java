package br.com.danielschiavo.sales.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import br.com.danielschiavo.catalog.dto.response.ShowProductsResponse;
import br.com.danielschiavo.catalog.service.product.ProductService;
import br.com.danielschiavo.sales.dto.request.AddCartItemRequest;
import br.com.danielschiavo.sales.dto.response.ShowCartItemResponse;
import br.com.danielschiavo.sales.dto.response.ShowCartResponse;
import br.com.danielschiavo.sales.mapper.CartMapper;
import br.com.danielschiavo.sales.model.entity.Cart;
import br.com.danielschiavo.sales.model.entity.CartItem;
import br.com.danielschiavo.sales.repository.CartRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;

@Service
@Setter
public class CartService {

	@Autowired
	private CartRepository repository;
	
	@Autowired
	private CartMapper mapper;

	@Transactional
	public void removeProductFromCart(Long customerId, Long... productIds) {
		List<Long> productIdsList = List.of(productIds);

		productIdsList.forEach(id -> {
			repository.findByCustomerIdAndCartItemsProductId(customerId, id)
					.orElseThrow(() -> new ValidationException("There's no item in the cart with provided id: " + id));
		});

		repository.deleteCartItemByCustomerIdAndProductId(customerId, productIdsList);
	}

	public List<ShowCartItemResponse> getCartItems(Long customerId) {
		Cart cart = repository.findByCustomerId(customerId)
				.orElseThrow(() -> new ValidationException("User does not have a cart"));
		return mapper.toDtoShowCartItem(cart.getCartItems());
	}

	@Transactional
	public void addProductToCart(Long customerId, AddCartItemRequest request) {
		repository.deleteCartItemByCustomerIdAndProductId(customerId, List.of(request.productId()));

		if (request.quantity() <= 0) {
			return;
        }

		Cart cart = repository.findByCustomerId(customerId).orElseThrow(() -> new ValidationException("Cart does not exist"));

		CartItem cartItem = CartItem.builder()
				.id(null)
				.quantity(request.quantity())
				.productId(request.productId())
				.insertionDateTime(LocalDateTime.now())
				.cart(cart).build();

		cart.addCartItem(cartItem);

		repository.save(cart);
	}

	public ShowCartResponse updateCart(Long customerId, List<ShowProductsResponse> products) {
		Cart cart = repository.findByCustomerId(customerId).orElseThrow(() -> new ValidationException("Cart does not exist"));
		List<CartItem> cartItems = cart.getCartItems();

		AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
		cartItems.forEach(item -> {
			var product = products.stream().filter(p -> p.getId().equals(item.getProductId())).findFirst().get();
			BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			item.setSubTotal(subTotal);

			total.updateAndGet(v -> v.add(subTotal));
		});

		cart.setTotalValue(total.get());

		return mapper.toDtoShowCart(repository.save(cart));
	}
}