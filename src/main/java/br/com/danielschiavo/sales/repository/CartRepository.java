package br.com.danielschiavo.sales.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.sales.model.entity.Cart;
import br.com.danielschiavo.sales.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository <Cart, Long>{

	@Query("SELECT c FROM Cart c JOIN c.cartItems items WHERE items.productId = :productId")
	Optional<List<Cart>> findAllCartsByProductId(@Param("productId") Long productId);

	Optional<Cart> findByCustomerId(Long customerId);

	Optional<List<CartItem>> findByCustomerIdAndCartItemsProductId(Long customerId, Long productId);

	@Query("DELETE FROM CartItem ci WHERE ci.cart.customerId = :customerId AND ci.productId IN :productId")
	void deleteCartItemByCustomerIdAndProductId(Long customerId, List<Long> productId);
}
