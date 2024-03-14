package br.com.danielschiavo.shop.infra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.tipoentregaproduto.TipoEntregaProduto;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;
import br.com.danielschiavo.shop.repositories.SubCategoriaRepository;

@Profile("dev")
@Component
public class InserirDadosMock implements CommandLineRunner {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private SubCategoriaRepository subCategoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private LimpadorBancoDeDados limpadorBancoDeDados;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		limpadorBancoDeDados.limpar();
		
		Cliente cliente = new Cliente(null, "14330283794", "Daniel", "Schiavo Rosseto", LocalDate.of(2000, 3, 3), LocalDate.now(), "daniel.schiavo35@gmail.com", "{noop}123456", "27996101055", null, null, null, null);
		Endereco endereco = new Endereco(null, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco3 = new Endereco(null, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		Cartao cartao = new Cartao(null, "Santander", "1123444255591132", "Daniel schiavo rosseto", "03/25", true, TipoCartao.CREDITO, cliente);
		cliente.setEnderecos(Arrays.asList(endereco, endereco3));
		cliente.setCartoes(Arrays.asList(cartao));
		
		Cliente cliente2 = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", null, null, null, null);
		Endereco endereco2 = new Endereco(null, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", true, cliente2);
		Cartao cartao2 = new Cartao(null, "Santander", "1111222244445555", "Silvana pereira da silva", "03/28", true, TipoCartao.CREDITO, cliente2);
		cliente2.setEnderecos(Arrays.asList(endereco2));
		cliente2.setCartoes(Arrays.asList(cartao2));
		
		List<Cliente> listUsuarios = Arrays.asList(cliente, cliente2);
		clienteRepository.saveAll(listUsuarios);
		
		
		Categoria categoria = new Categoria(null, "Computadores", null);
		Categoria categoria2 = new Categoria(null, "Suporte", null);
		Categoria categoria3 = new Categoria(null, "Softwares", null);
		
		SubCategoria subCategoria = new SubCategoria(null, "Teclado", categoria);
		SubCategoria subCategoria2 = new SubCategoria(null, "Mouse", categoria);
		SubCategoria subCategoria3 = new SubCategoria(null, "Mousepad", categoria);
		SubCategoria subCategoria4 = new SubCategoria(null, "SSD", categoria);
		SubCategoria subCategoria5 = new SubCategoria(null, "Placa de video", categoria);
		
		SubCategoria subCategoria6 = new SubCategoria(null, "Instalacao windows", categoria2);
		SubCategoria subCategoria7 = new SubCategoria(null, "Problemas em geral", categoria2);
		
		SubCategoria subCategoria8 = new SubCategoria(null, "Sistema administrativo", categoria3);
		SubCategoria subCategoria9 = new SubCategoria(null, "Automacao", categoria3);
		
		categoriaRepository.saveAll(Arrays.asList(categoria, categoria2, categoria3));
		subCategoriaRepository.saveAll(Arrays.asList(subCategoria, subCategoria2, subCategoria3,subCategoria4,subCategoria5,subCategoria6,subCategoria7,subCategoria8,subCategoria9));
		
		
		Produto produto = new Produto(null, 
				"Teclado RedDragon switch vermelho", 
				"Teclado reddragon, switch vermelho, sem teclado numérico pt-br, com leds, teclas macro, switch óptico, teclas anti-desgaste", 
				BigDecimal.valueOf(200.00), 
				5, 
				true, 
				null,
				null,
				categoria, 
				subCategoria);
		ArquivosProduto arquivosProduto = new ArquivosProduto(null, "APID1POS0.jpeg", (byte) 0, produto);
		ArquivosProduto arquivosProduto2 = new ArquivosProduto(null, "APID1POS1.jpeg", (byte) 1, produto);
		List<ArquivosProduto> listArquivosProduto = Arrays.asList(arquivosProduto, arquivosProduto2);
		
		TipoEntregaProduto tipoEntregaProduto = new TipoEntregaProduto(null, TipoEntrega.CORREIOS, produto);
		TipoEntregaProduto tipoEntregaProduto2 = new TipoEntregaProduto(null, TipoEntrega.ENTREGA_EXPRESSA, produto);
		TipoEntregaProduto tipoEntregaProduto3 = new TipoEntregaProduto(null, TipoEntrega.RETIRADA_NA_LOJA, produto);
		Set<TipoEntregaProduto> tiposEntrega = Set.of(tipoEntregaProduto, tipoEntregaProduto2, tipoEntregaProduto3);
		
		produto.setArquivosProduto(listArquivosProduto);
		produto.setTiposEntrega(tiposEntrega);
	
		
		Produto produto2 = new Produto(null, 
				"Sistema Digisat Administrador", 
				"Sistema administrativo para empresas, completo", 
				BigDecimal.valueOf(1000.00), 
				999, 
				true, 
				null, 
				null,
				categoria3, 
				subCategoria8);
		ArquivosProduto arquivosProduto3 = new ArquivosProduto(null, "APID2POS0.jpeg", (byte) 0, produto2);
		ArquivosProduto arquivosProduto4 = new ArquivosProduto(null, "APID2POS1.jpeg", (byte) 1, produto2);
		ArquivosProduto arquivosProduto5 = new ArquivosProduto(null, "APID3POS2.jpeg", (byte) 2, produto2);
		List<ArquivosProduto> listArquivosProduto2 = Arrays.asList(arquivosProduto3, arquivosProduto4, arquivosProduto5);
		
		TipoEntregaProduto tipoEntregaProduto4 = new TipoEntregaProduto(null, TipoEntrega.RETIRADA_NA_LOJA, produto2);
		TipoEntregaProduto tipoEntregaProduto5 = new TipoEntregaProduto(null, TipoEntrega.CORREIOS, produto2);
		TipoEntregaProduto tipoEntregaProduto6 = new TipoEntregaProduto(null, TipoEntrega.ENTREGA_EXPRESSA, produto2);
		Set<TipoEntregaProduto> tipoEntrega2 = Set.of(tipoEntregaProduto4, tipoEntregaProduto5, tipoEntregaProduto6);
		
		produto2.setArquivosProduto(listArquivosProduto2);
		produto2.setTiposEntrega(tipoEntrega2);

		produtoRepository.saveAll(Arrays.asList(produto, produto2));
		
		
		Carrinho carrinho = new Carrinho(null, null, cliente);
		
		ItemCarrinho itemCarrinho = new ItemCarrinho(null, 3, produto, carrinho);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(null, 1, produto2, carrinho);
		
		carrinho.setItemsCarrinho(Arrays.asList(itemCarrinho, itemCarrinho2));
		
		Carrinho carrinho2 = new Carrinho(null, null, cliente2);
		
		ItemCarrinho itemCarrinho3 = new ItemCarrinho(null, 3, produto, carrinho2);
		ItemCarrinho itemCarrinho4 = new ItemCarrinho(null, 1, produto2, carrinho2);
		
		carrinho2.setItemsCarrinho(Arrays.asList(itemCarrinho3, itemCarrinho4));
		
		carrinhoRepository.saveAll(Arrays.asList(carrinho, carrinho2));
	}

}
