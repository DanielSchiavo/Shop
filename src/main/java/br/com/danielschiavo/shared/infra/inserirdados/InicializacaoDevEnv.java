package br.com.danielschiavo.shared.infra.inserirdados;

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
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.pedido.repository.PedidoRepository;
import br.com.danielschiavo.produto.model.entity.Produto;
import br.com.danielschiavo.produto.model.valueobject.ArquivoProduto;
import br.com.danielschiavo.produto.model.entity.Categoria;
import br.com.danielschiavo.produto.model.entity.SubCategoria;
import br.com.danielschiavo.produto.model.enums.TipoEntregaProduto;
import br.com.danielschiavo.produto.repository.CategoriaRepository;
import br.com.danielschiavo.produto.repository.ProdutoRepository;
import br.com.danielschiavo.produto.repository.SubCategoriaRepository;
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
public class InicializacaoDevEnv implements CommandLineRunner {
	
	@Autowired
	private CustomerRepository clienteRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private LimpadorBancoDeDados limpadorBancoDeDados;
	
	private final Produto.ProdutoBuilder produtoBuilder = Produto.builder();

	private final Categoria.CategoriaBuilder categoriaBuilder = Categoria.builder();
	private final SubCategoria.SubCategoriaBuilder subCategoriaBuilder = SubCategoria.builder();

	
	private final Address.AddressBuilder enderecoBuilder = Address.builder();
	private final Card.CardBuilder cartaoBuilder = Card.builder();
	private final Customer.CustomerBuilder clienteBuilder = Customer.builder();
	private final RoleBuilder roleBuilder = Role.builder();
	
	private final Pedido.PedidoBuilder pedidoBuilder = Pedido.builder();

    @Autowired
    private SubCategoriaRepository subCategoriaRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private AddressRepository addressRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		limpadorBancoDeDados.limpar();
		
		inserirDados();
	}

	public void inserirDados() {


		Categoria software = categoriaBuilder.nome("Software").build();
		Categoria categoria = categoriaRepository.save(software);
		SubCategoria subCategoriaTeclado = subCategoriaRepository.save(subCategoriaBuilder.categoriaId(categoria.getId()).nome("Teclado").build());
		SubCategoria subCategoriaMouse = subCategoriaRepository.save(subCategoriaBuilder.categoriaId(categoria.getId()).nome("Mouse").build());
		subCategoriaRepository.save(subCategoriaBuilder.categoriaId(categoria.getId()).nome("SSD").build());
		subCategoriaRepository.save(subCategoriaBuilder.categoriaId(categoria.getId()).nome("Placa de Video").build());

		Categoria computadores = categoriaBuilder.nome("Computadores").build();
		Categoria categoria1 = categoriaRepository.save(computadores);
		subCategoriaRepository.save(subCategoriaBuilder.categoriaId(categoria1.getId()).nome("Sistema Administrativo").build());
		subCategoriaRepository.save(subCategoriaBuilder.categoriaId(categoria1.getId()).nome("Automacao").build());

		 Produto produto = produtoBuilder.id(null)
									 	  .nome("Teclado RedDragon switch vermelho")
										  .descricao("Teclado reddragon, switch vermelho, sem teclado numérico pt-br, com leds, teclas macro, switch óptico, teclas anti-desgaste")
										  .preco(BigDecimal.valueOf(200.00))
										  .quantidade(999)
										  .ativo(true)
										  .subCategoriaId(subCategoriaTeclado.getId()).build();
		
		 TipoEntregaProduto tipoEntregaProduto = TipoEntregaProduto.builder()
																   .tipoEntrega(TipoEntrega.RETIRADA_NA_LOJA)
																   .produto(produto).build();
		 
		 produto.adicionarTipoEntrega(tipoEntregaProduto);
		 
		 ArquivoProduto arquivoProduto = ArquivoProduto.builder()
				 									   .nome("Padrao.jpeg")
				 									   .posicao((byte) 0)
				 									   .produto(produto).build();
		 
		 ArquivoProduto arquivoProduto3 = ArquivoProduto.builder()
													   .nome("teste.jpeg")
													   .posicao((byte) 1)
													   .produto(produto).build();
		 
		 produto.adicionarArquivoProduto(arquivoProduto);
		 produto.adicionarArquivoProduto(arquivoProduto3);
		 
		 
		 
		 Produto produto2 = produtoBuilder.id(null)
									 	  .nome("Mouse RedDragon")
										  .descricao("Descricao mouse reddragon")
										  .preco(BigDecimal.valueOf(200.00))
										  .quantidade(999)
										  .ativo(true)
										  .subCategoriaId(subCategoriaMouse.getId())
										  .build();
		 
		 TipoEntregaProduto tipoEntregaProduto2 = TipoEntregaProduto.builder()
																   .tipoEntrega(TipoEntrega.RETIRADA_NA_LOJA)
																   .produto(produto).build();

		produto2.adicionarTipoEntrega(tipoEntregaProduto2);
		
		ArquivoProduto arquivoProduto2 = ArquivoProduto.builder()
													   .nome("Padrao.jpeg")
													   .posicao((byte) 0)
													   .produto(produto2).build();
												
		produto2.adicionarArquivoProduto(arquivoProduto2);
		 
		 
		
		produtoRepository.saveAll(List.of(produto, produto2));

		Customer customer = clienteBuilder
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
							   .dataEHoraAtribuicao(LocalDateTime.now())
							   .customer(customer).build();
		
		Address address = enderecoBuilder.id(null)
										  .postalCode("29142298")
										  .state("NaoSeiONome")
										  .number("15")
										  .complement(null)
										  .neighborhood("Itapua")
										  .city("Vila Velha")
										  .state("ES")
										  .isDefault(true)
										  .customerId(customer.getId()).build();
		
		Card card = cartaoBuilder.id(null)
									  .bankName("Santander")
									  .cardNumber("1123444255591132")
									  .nameOnCard("Daniel Schiavo Rosseto")
									  .expirationDate("03/25")
									  .isDefault(true)
									  .cardType(CardType.CREDIT)
									  .customerId(customer.getId()).build();
		
		customer.adicionarRole(role);

		Customer customer2 = clienteBuilder.id(null)
										.cpf("12345678994")
										.name("Silvana")
										.surname("Pereira da silva")
										.birthDate(LocalDate.of(2000, 5, 3))
										.accountCreationDate(LocalDate.now())
										.email("silvana.dasilva@gmail.com")
										.password("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
										.cellphoneNumber("27999833653")
										.profilePicture("Padrao.jpeg").build();
		
		Address address2 = enderecoBuilder.id(null)
											  .postalCode("29142298")
											  .street("Avenida luciano das neves")
											  .number("3233")
											  .complement("Apartamento 302")
											  .neighborhood("Praia de itaparica")
											  .city("Vila Velha")
											  .state("ES")
											  .isDefault(true)
											  .customerId(customer2.getId()).build();
		
		Card card2 = cartaoBuilder.id(null)
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
//		List<Pedido> pedidos = pedidoBuilder
//				.customer(clientes.get(0))
//					 .comItemPedidoIdQuantidadeProduto(null, 2, produtos.get(0))
//					 .pagamentoIdMetodo(null, MetodoPagamento.PIX)
//					 .entregaIdTipo(null, TipoEntrega.ENTREGA_DIGITAL)
//				.customer(clientes.get(1))
//					 .comItemPedidoIdQuantidadeProduto(null, 2, produtos.get(1))
//					 .pagamentoIdMetodo(null, MetodoPagamento.PIX)
//					 .entregaIdTipo(null, TipoEntrega.ENTREGA_DIGITAL)
//					 .getPedidos();
//		
//			
//		pedidoRepository.saveAll(pedidos);
		
	}
}
