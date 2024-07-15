package br.com.danielschiavo.sales.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.service.product.ProductService;
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
    private ProductService produtoService;

	@Transactional
	public void removeProductFromCart(Long customerId, Long... productsId) {
		Cart cart = getCartByCustomerId(customerId);

		List<CartItem> cartItems = cart.getCartItems();
		cartItems.stream().filter(item -> Arrays.stream(productsId).anyMatch(id -> item.getProductId().equals(id)))
				.forEach(cart::removeCartItem);

		repository.save(cart);
	}

	public Cart getCartByCustomerId(Long customerId) {
		return repository.findByCustomerId(customerId).orElseThrow(() -> new ValidationException("User does not have a cart"));
	}

	@Transactional
	public Cart addProductToCart(Long customerId, CartItem addCartItem) {
		if (addCartItem.getQuantity() <= 0) {
			throw new ValidationException("The product quantity must be greater than or equal to 1, the value provided was: "
					+ addCartItem.getQuantity());
		}

		Cart cart = getCartByCustomerId(customerId);
		cart.setUpdateDateTime(LocalDateTime.now());

		List<CartItem> cartItems = cart.getCartItems();
		Optional<CartItem> optionalCartItem = cartItems.stream().filter(item -> item.getProductId().equals(addCartItem.getProductId())).findFirst();

		if (optionalCartItem.isPresent()) {
			var cartItem = optionalCartItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + addCartItem.getQuantity());
		} else {
			Product product = produtoService.getProductById(addCartItem.getProductId());
			BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(addCartItem.getQuantity()));
			CartItem cartItem = CartItem.builder()
					.id(null)
					.quantity(addCartItem.getQuantity())
					.productId(addCartItem.getProductId())
					.subTotal(subTotal)
					.insertionDateTime(LocalDateTime.now())
					.cart(cart).build();

			// In the addItemToCart method, the new total value in the Cart entity is already set
			cart.addCartItem(cartItem);
		}

		return repository.save(cart);
	}

	@Transactional
	public void setProductQuantityInCart(Long customerId, CartItem updatedCartItem) {
		Cart cart = getCartByCustomerId(customerId);

		CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getProductId().equals(updatedCartItem.getProductId()))
				.findFirst().orElseThrow(() -> new ValidationException("This product has not been added to the cart yet"));

		if (updatedCartItem.getQuantity() <= 0) {
			cart.removeCartItem(cartItem);
		} else {
			cartItem.setQuantity(updatedCartItem.getQuantity());
		}

		repository.save(cart);
	}
}