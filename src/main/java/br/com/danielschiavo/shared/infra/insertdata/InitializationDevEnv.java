package br.com.danielschiavo.shared.infra.insertdata;

import br.com.danielschiavo.catalog.model.enums.ProductFileType;
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
import br.com.danielschiavo.filestorage.model.FileReference;
import br.com.danielschiavo.filestorage.model.FileReferenceKey;
import br.com.danielschiavo.filestorage.model.FileType;
import br.com.danielschiavo.filestorage.repository.FileReferenceRepository;
import br.com.danielschiavo.order.model.entity.Order;
import br.com.danielschiavo.delivery.model.enums.DeliveryType;
import br.com.danielschiavo.order.repository.OrderRepository;
import br.com.danielschiavo.catalog.model.entity.Category;
import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import br.com.danielschiavo.catalog.model.enums.ProductDeliveryType;
import br.com.danielschiavo.catalog.repository.CategoryRepository;
import br.com.danielschiavo.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Profile("dev")
@Component
public class InitializationDevEnv implements CommandLineRunner {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CategoryRepository categoriaRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private FileReferenceRepository fileRepository;
	
	private final Product.ProductBuilder productBuilder = Product.builder();

	private final Category.CategoryBuilder categoryBuilder = Category.builder();

	
	private final Address.AddressBuilder addressBuilder = Address.builder();
	private final Card.CardBuilder cardBuilder = Card.builder();
	private final Customer.CustomerBuilder customerBuilder = Customer.builder();
	private final RoleBuilder roleBuilder = Role.builder();
	
	private final Order.OrderBuilder orderBuilder = Order.builder();

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

		Category computadores = categoryBuilder.name("Computadores").build();
		Category category1 = categoriaRepository.save(computadores);

		 Product produto = productBuilder.id(null)
									 	  .name("Teclado RedDragon switch vermelho")
										  .description("Teclado reddragon, switch vermelho, sem teclado numérico pt-br, com leds, teclas macro, switch óptico, teclas anti-desgaste")
										  .price(BigDecimal.valueOf(200.00))
										  .quantity(999)
										  .active(true)
										  .categoryId(category1.getId()).build();
		
		 ProductDeliveryType productDeliveryType = ProductDeliveryType.builder()
																   .deliveryType(DeliveryType.PICK_UP_IN_STORE)
																   .product(produto).build();
		 
		 produto.addDeliveryType(productDeliveryType);
		 
		 ProductFile productFile = ProductFile.builder()
				 .fileName("Default.jpeg")
				 .type(ProductFileType.IMAGE)
				 .position((byte) 0)
				 .product(produto)
				 .build();
		 
		 ProductFile productFile3 = ProductFile.builder()
				 .fileName("Default.jpeg")
				 .type(ProductFileType.IMAGE)
				 .position((byte) 1)
				 .product(produto)
				 .build();
		 
		 produto.addProductFile(productFile);
		 produto.addProductFile(productFile3);
		 
		 
		 
		 Product produto2 = productBuilder.id(null)
									 	  .name("Mouse RedDragon")
										  .description("Descricao mouse reddragon")
										  .price(BigDecimal.valueOf(200.00))
										  .quantity(999)
										  .active(true)
										  .categoryId(category.getId())
										  .build();
		 
		 ProductDeliveryType productDeliveryType2 = ProductDeliveryType.builder()
																   .deliveryType(DeliveryType.PICK_UP_IN_STORE)
																   .product(produto).build();

		produto2.addDeliveryType(productDeliveryType2);
		
		ProductFile productFile2 = ProductFile.builder()
				.fileName("Default.jpeg")
				.type(ProductFileType.IMAGE)
				.position((byte) 0)
				.product(produto2)
				.build();
												
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
						.profilePicture("profiles/Default.jpeg")
						.build();
		
		Role role = roleBuilder.id(null)
							   .role(RoleName.ADMIN)
							   .assignmentDateTime(LocalDateTime.now())
							   .customer(customer).build();
		
		Address address = addressBuilder.id(null)
										  .postalCode("29142298")
										  .street("NaoSeiONome")
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

		Customer customer2 = customerBuilder.id(2L)
										.cpf("12345678994")
										.name("Silvana")
										.surname("Pereira da silva")
										.birthDate(LocalDate.of(2000, 5, 3))
										.accountCreationDate(LocalDate.now())
										.email("silvana.dasilva@gmail.com")
										.password("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
										.cellphoneNumber("27999833653")
										.profilePicture("profiles/Default.jpeg").build();
		
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
		

		customerRepository.saveAll(List.of(customer, customer2));
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

		FileReference fileReference = FileReference.builder()
				.contentType("image/jpeg")
				.file(new FileReferenceKey("products", "Default.jpeg"))
				.contentLength(7200L)
				.createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC))
				.isPublicAccessible(true)
				.type(FileType.IMAGE)
				.temp(false)
				.build();

		FileReference fileReference2 = FileReference.builder()
				.contentType("image/jpeg")
				.file(new FileReferenceKey("orders", "Default.jpeg"))
				.contentLength(7200L)
				.createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC))
				.isPublicAccessible(true)
				.type(FileType.IMAGE)
				.temp(false)
				.build();

		FileReference fileReference3 = FileReference.builder()
				.contentType("image/jpeg")
				.file(new FileReferenceKey("profiles", "Default.jpeg"))
				.contentLength(19200L)
				.createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC))
				.isPublicAccessible(true)
				.type(FileType.IMAGE)
				.temp(false)
				.build();

		fileRepository.saveAll(List.of(fileReference, fileReference2, fileReference3));
	}
}
