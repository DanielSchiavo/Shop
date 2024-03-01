package br.com.danielschiavo.shop.infra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
import br.com.danielschiavo.shop.models.produto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;
import br.com.danielschiavo.shop.repositories.SubCategoriaRepository;

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

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Cliente usuario = new Cliente(null, "14330283794", "Daniel", "Schiavo Rosseto", LocalDate.of(2000, 3, 3), LocalDate.now(), "daniel.schiavo35@gmail.com", "{noop}123456", "27996101055", null, null, null);
		Endereco endereco = new Endereco(null, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, usuario);
		Endereco endereco3 = new Endereco(null, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", true, usuario);
		Cartao cartao = new Cartao(null, "Santander", "1123444255591132", "Daniel schiavo rosseto", "03/25", true, TipoCartao.CREDITO, usuario);
		usuario.setEnderecos(Arrays.asList(endereco, endereco3));
		usuario.setCartoes(Arrays.asList(cartao));
		
		Cliente usuario2 = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", null, null, null);
		Endereco endereco2 = new Endereco(null, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", true, usuario2);
		Cartao cartao2 = new Cartao(null, "Santander", "1111222244445555", "Silvana pereira da silva", "03/28", true, TipoCartao.CREDITO, usuario2);
		usuario2.setEnderecos(Arrays.asList(endereco2));
		usuario2.setCartoes(Arrays.asList(cartao2));
		
		List<Cliente> listUsuarios = Arrays.asList(usuario, usuario2);
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
		
		
		ArquivosProduto arquivosProduto = new ArquivosProduto("APID1POS0.jpeg", 0);
		ArquivosProduto arquivosProduto2 = new ArquivosProduto("APID1POS1.jpeg", 1);
		List<ArquivosProduto> listArquivosProduto = Arrays.asList(arquivosProduto, arquivosProduto2);
		Set<TipoEntrega> tipoEntrega = Set.of(TipoEntrega.CORREIOS, TipoEntrega.ENTREGA_EXPRESSA, TipoEntrega.RETIRADA_NA_LOJA);
		Produto produto = new Produto(null, "Teclado RedDragon switch vermelho", "Teclado reddragon, switch vermelho, sem teclado numérico pt-br, com leds, teclas macro, switch óptico, teclas anti-desgaste", BigDecimal.valueOf(200.00), 5, true, subCategoria, categoria, tipoEntrega, listArquivosProduto);
	
		ArquivosProduto arquivosProduto3 = new ArquivosProduto("APID2POS0.jpeg", 0);
		ArquivosProduto arquivosProduto4 = new ArquivosProduto("APID2POS1.jpeg", 1);
		ArquivosProduto arquivosProduto5 = new ArquivosProduto("APID3POS2.jpeg", 2);
		List<ArquivosProduto> listArquivosProduto2 = Arrays.asList(arquivosProduto3, arquivosProduto4, arquivosProduto5);
		Set<TipoEntrega> tipoEntrega2 = Set.of(TipoEntrega.ENTREGA_DIGITAL);
		Produto produto2 = new Produto(null, "Sistema Digisat Administrador", "Sistema administrativo para empresas, completo", BigDecimal.valueOf(1000.00), 999, true, subCategoria8, categoria3, tipoEntrega2, listArquivosProduto2);

		produtoRepository.saveAll(Arrays.asList(produto, produto2));
		
		ItemCarrinho itemCarrinho = new ItemCarrinho(produto.getId(), 3);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(produto2.getId(), 1);
		Carrinho carrinho = new Carrinho(null, usuario, Arrays.asList(itemCarrinho, itemCarrinho2));
		
		carrinhoRepository.save(carrinho);
	}

}
