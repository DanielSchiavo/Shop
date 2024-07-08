package br.com.danielschiavo.shared.infra.inserirdados;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.model.enums.TipoCartao;
import br.com.danielschiavo.cliente.model.entity.Endereco;
import br.com.danielschiavo.cliente.model.enums.NomeRole;
import br.com.danielschiavo.cliente.model.entity.Role;
import br.com.danielschiavo.cliente.model.entity.Role.RoleBuilder;
import br.com.danielschiavo.cliente.repository.ClienteRepository;
import br.com.danielschiavo.pedido.model.Pedido;
import br.com.danielschiavo.pedido.model.TipoEntrega;
import br.com.danielschiavo.pedido.repository.admin.PedidoRepository;
import br.com.danielschiavo.produto.model.Produto;
import br.com.danielschiavo.produto.model.arquivosproduto.ArquivoProduto;
import br.com.danielschiavo.produto.model.categoria.Categoria;
import br.com.danielschiavo.produto.model.categoria.subcategoria.SubCategoria;
import br.com.danielschiavo.produto.model.tipoentregaproduto.TipoEntregaProduto;
import br.com.danielschiavo.produto.repository.admin.CategoriaRepository;
import br.com.danielschiavo.produto.repository.admin.ProdutoRepository;
import br.com.danielschiavo.produto.repository.user.SubCategoriaRepository;
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
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private LimpadorBancoDeDados limpadorBancoDeDados;
	
	private Produto.ProdutoBuilder produtoBuilder = Produto.builder();

	private Categoria.CategoriaBuilder categoriaBuilder = Categoria.builder();
	private SubCategoria.SubCategoriaBuilder subCategoriaBuilder = SubCategoria.builder();

	
	private Endereco.EnderecoBuilder enderecoBuilder = Endereco.builder();
	private Cartao.CartaoBuilder cartaoBuilder = Cartao.builder();
	private Cliente.ClienteBuilder clienteBuilder = Cliente.builder();
	private RoleBuilder roleBuilder = Role.builder();
	
	private Pedido.PedidoBuilder pedidoBuilder = Pedido.builder();
    @Autowired
    private SubCategoriaRepository subCategoriaRepository;

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

		Cliente cliente = clienteBuilder
						.id(1L)
						.cpf("12345678912")
						.nome("Daniel")
						.sobrenome("Schiavo Rosseto")
						.dataNascimento(LocalDate.of(2000, 3, 3))
						.dataCriacaoConta(LocalDate.now())
						.email("daniel.schiavo35@gmail.com")
						.senha("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
						.celular("27996121255")
						.fotoPerfil("Padrao.jpeg")
						.build();
		
		Role role = roleBuilder.id(null)
							   .role(NomeRole.ADMIN)
							   .dataEHoraAtribuicao(LocalDateTime.now())
							   .cliente(cliente).build();
		
		Endereco endereco = enderecoBuilder.id(null)
										  .cep("29142298")
										  .rua("NaoSeiONome")
										  .numero("15")
										  .complemento(null)
										  .bairro("Itapua")
										  .cidade("Vila Velha")
										  .estado("ES")
										  .enderecoPadrao(true)
										  .cliente(cliente).build();
		
		Cartao cartao = cartaoBuilder.id(null)
									  .nomeBanco("Santander")
									  .numeroCartao("1123444255591132")
									  .nomeNoCartao("Daniel Schiavo Rosseto")
									  .validadeCartao("03/25")
									  .cartaoPadrao(true)
									  .tipoCartao(TipoCartao.CREDITO)
									  .cliente(cliente).build();
		
		cliente.adicionarRole(role);
		cliente.adicionarEndereco(endereco);
		cliente.adicionarCartao(cartao);
		
		Cliente cliente2 = clienteBuilder.id(null)
										.cpf("12345678994")
										.nome("Silvana")
										.sobrenome("Pereira da silva")
										.dataNascimento(LocalDate.of(2000, 5, 3))
										.dataCriacaoConta(LocalDate.now())
										.email("silvana.dasilva@gmail.com")
										.senha("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
										.celular("27999833653")
										.fotoPerfil("Padrao.jpeg").build();
		
		Endereco endereco2 = enderecoBuilder.id(null)
											  .cep("29142298")
											  .rua("Avenida luciano das neves")
											  .numero("3233")
											  .complemento("Apartamento 302")
											  .bairro("Praia de itaparica")
											  .cidade("Vila Velha")
											  .estado("ES")
											  .enderecoPadrao(true)
											  .cliente(cliente2).build();
		
		Cartao cartao2 = cartaoBuilder.id(null)
									  .nomeBanco("Santander")
									  .numeroCartao("1111222244445555")
									  .nomeNoCartao("Silvana pereira da silva")
									  .validadeCartao("03/28")
									  .cartaoPadrao(true)
									  .tipoCartao(TipoCartao.CREDITO)
									  .cliente(cliente2).build();
		
		cliente2.adicionarCartao(cartao2);
		cliente2.adicionarEndereco(endereco2);
		
		
		clienteRepository.saveAll(List.of(cliente, cliente2));
//		
//		List<Pedido> pedidos = pedidoBuilder
//				.cliente(clientes.get(0))
//					 .comItemPedidoIdQuantidadeProduto(null, 2, produtos.get(0))
//					 .pagamentoIdMetodo(null, MetodoPagamento.PIX)
//					 .entregaIdTipo(null, TipoEntrega.ENTREGA_DIGITAL)
//				.cliente(clientes.get(1))
//					 .comItemPedidoIdQuantidadeProduto(null, 2, produtos.get(1))
//					 .pagamentoIdMetodo(null, MetodoPagamento.PIX)
//					 .entregaIdTipo(null, TipoEntrega.ENTREGA_DIGITAL)
//					 .getPedidos();
//		
//			
//		pedidoRepository.saveAll(pedidos);
		
	}
}
