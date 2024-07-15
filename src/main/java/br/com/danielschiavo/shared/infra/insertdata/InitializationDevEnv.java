package br.com.danielschiavo.shared.infra.insertdata;

import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.enums.CardType;
import br.com.danielschiavo.customer.model.entity.Address;
import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.model.valueobject.Role;
import br.com.danielschiavo.customer.model.valueobject.Role.RoleBuilder;
import br.com.danielschiavo.customer.repository.CardRepository;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import br.com.danielschiavo.customer.repository.AddressRepository;
import br.com.danielschiavo.pedido.model.entity.Order;
import br.com.danielschiavo.pedido.model.enums.DeliveryType;
import br.com.danielschiavo.pedido.repository.OrderRepository;
import br.com.danielschiavo.produto.model.entity.Category;
import br.com.danielschiavo.produto.model.entity.Product;
import br.com.danielschiavo.produto.model.entity.SubCategory;
import br.com.danielschiavo.produto.model.valueobject.ProductFile;
import br.com.danielschiavo.produto.model.enums.ProductDeliveryType;
import br.com.danielschiavo.produto.repository.CategoryRepository;
import br.com.danielschiavo.produto.repository.ProductRepository;
import br.com.danielschiavo.produto.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Profile("dev")
@Component
public class InitializationDevEnv implements CommandLineRunner {
	
	@Autowired
	private CustomerRepository clienteRepository;
	
	@Autowired
	private CategoryRepository categoriaRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	private final Product.ProductBuilder productBuilder = Product.builder();

	private final Category.CategoryBuilder categoryBuilder = Category.builder();
	private final SubCategory.SubCategoryBuilder subCategoryBuilder = SubCategory.builder();

	
	private final Address.AddressBuilder addressBuilder = Address.builder();
	private final Card.CardBuilder cardBuilder = Card.builder();
	private final Customer.CustomerBuilder customerBuilder = Customer.builder();
	private final RoleBuilder roleBuilder = Role.builder();
	
	private final Order.OrderBuilder orderBuilder = Order.builder();

    @Autowired
    private SubCategoryRepository subCategoriaRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private AddressRepository addressRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		databaseCleaner.clean();
		
		insertData();
	}

	public void insertData() {
		Category software = categoryBuilder.name("Software").build();
		Category category = categoriaRepository.save(software);
		SubCategory subCategoryTeclado = subCategoriaRepository.save(subCategoryBuilder.categoryId(category.getId()).name("Teclado").build());
		SubCategory subCategoryMouse = subCategoriaRepository.save(subCategoryBuilder.categoryId(category.getId()).name("Mouse").build());
		subCategoriaRepository.save(subCategoryBuilder.categoryId(category.getId()).name("SSD").build());
		subCategoriaRepository.save(subCategoryBuilder.categoryId(category.getId()).name("Placa de Video").build());

		Category computadores = categoryBuilder.name("Computadores").build();
		Category category1 = categoriaRepository.save(computadores);
		subCategoriaRepository.save(subCategoryBuilder.categoryId(category1.getId()).name("Sistema Administrativo").build());
		subCategoriaRepository.save(subCategoryBuilder.categoryId(category1.getId()).name("Automacao").build());

		 Product produto = productBuilder.id(null)
									 	  .name("Teclado RedDragon switch vermelho")
										  .description("Teclado reddragon, switch vermelho, sem teclado numérico pt-br, com leds, teclas macro, switch óptico, teclas anti-desgaste")
										  .price(BigDecimal.valueOf(200.00))
										  .quantity(999)
										  .active(true)
										  .subCategoryId(subCategoryTeclado.getId()).build();
		
		 ProductDeliveryType productDeliveryType = ProductDeliveryType.builder()
																   .deliveryType(DeliveryType.PICK_UP_IN_STORE)
																   .product(produto).build();
		 
		 produto.addDeliveryType(productDeliveryType);
		 
		 ProductFile productFile = ProductFile.builder()
				 									   .name("Padrao.jpeg")
				 									   .position((byte) 0)
				 									   .product(produto).build();
		 
		 ProductFile productFile3 = ProductFile.builder()
													   .name("teste.jpeg")
													   .position((byte) 1)
													   .product(produto).build();
		 
		 produto.addProductFile(productFile);
		 produto.addProductFile(productFile3);
		 
		 
		 
		 Product produto2 = productBuilder.id(null)
									 	  .name("Mouse RedDragon")
										  .description("Descricao mouse reddragon")
										  .price(BigDecimal.valueOf(200.00))
										  .quantity(999)
										  .active(true)
										  .subCategoryId(subCategoryMouse.getId())
										  .build();
		 
		 ProductDeliveryType productDeliveryType2 = ProductDeliveryType.builder()
																   .deliveryType(DeliveryType.PICK_UP_IN_STORE)
																   .product(produto).build();

		produto2.addDeliveryType(productDeliveryType2);
		
		ProductFile productFile2 = ProductFile.builder()
													   .name("Padrao.jpeg")
													   .position((byte) 0)
													   .product(produto2).build();
												
		produto2.addProductFile(productFile2);
		 
		 
		
		productRepository.saveAll(List.of(produto, produto2));

		Customer customer = customerBuilder
						.id(1L)
						.cpf("12345678912")
						.name("Daniel")
						.surname("Schiavo Rosseto")
						.birthDate(LocalDate.of(2000, 3, 3))
						.accountCreationDate(LocalDate.now())
						.email("daniel.schiavo35@gmail.com")
						.password("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
						.cellphoneNumber("27996121255")
						.profilePicture("Padrao.jpeg")
						.build();
		
		Role role = roleBuilder.id(null)
							   .role(RoleName.ADMIN)
							   .assignmentDateTime(LocalDateTime.now())
							   .customer(customer).build();
		
		Address address = addressBuilder.id(null)
										  .postalCode("29142298")
										  .state("NaoSeiONome")
										  .number("15")
										  .complement(null)
										  .neighborhood("Itapua")
										  .city("Vila Velha")
										  .state("ES")
										  .isDefault(true)
										  .customerId(customer.getId()).build();
		
		Card card = cardBuilder.id(null)
									  .bankName("Santander")
									  .cardNumber("1123444255591132")
									  .nameOnCard("Daniel Schiavo Rosseto")
									  .expirationDate("03/25")
									  .isDefault(true)
									  .cardType(CardType.CREDIT)
									  .customerId(customer.getId()).build();
		
		customer.adicionarRole(role);

		Customer customer2 = customerBuilder.id(null)
										.cpf("12345678994")
										.name("Silvana")
										.surname("Pereira da silva")
										.birthDate(LocalDate.of(2000, 5, 3))
										.accountCreationDate(LocalDate.now())
										.email("silvana.dasilva@gmail.com")
										.password("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
										.cellphoneNumber("27999833653")
										.profilePicture("Padrao.jpeg").build();
		
		Address address2 = addressBuilder.id(null)
											  .postalCode("29142298")
											  .street("Avenida luciano das neves")
											  .number("3233")
											  .complement("Apartamento 302")
											  .neighborhood("Praia de itaparica")
											  .city("Vila Velha")
											  .state("ES")
											  .isDefault(true)
											  .customerId(customer2.getId()).build();
		
		Card card2 = cardBuilder.id(null)
									  .bankName("Santander")
									  .cardNumber("1111222244445555")
									  .nameOnCard("Silvana pereira da silva")
									  .expirationDate("03/28")
									  .isDefault(true)
									  .cardType(CardType.CREDIT)
									  .customerId(customer2.getId()).build();
		

		clienteRepository.saveAll(List.of(customer, customer2));
		addressRepository.saveAll(List.of(address, address2));
		cardRepository.saveAll(List.of(card, card2));
//		
//		List<Order> pedidos = pedidoBuilder
//				.customer(clientes.get(0))
//					 .comItemPedidoIdQuantidadeProduto(null, 2, products.get(0))
//					 .pagamentoIdMetodo(null, PaymentMethod.PIX)
//					 .entregaIdTipo(null, DeliveryType.ENTREGA_DIGITAL)
//				.customer(clientes.get(1))
//					 .comItemPedidoIdQuantidadeProduto(null, 2, products.get(1))
//					 .pagamentoIdMetodo(null, PaymentMethod.PIX)
//					 .entregaIdTipo(null, DeliveryType.ENTREGA_DIGITAL)
//					 .getPedidos();
//		
//			
//		orderRepository.saveAll(pedidos);
		
	}
}
