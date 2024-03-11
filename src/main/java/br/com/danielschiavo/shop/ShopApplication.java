package br.com.danielschiavo.shop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.Endereco;

@SpringBootApplication
public class ShopApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
		
		final Path raizPerfil = Path.of(System.getProperty("user.home") + "/.shop/" + "imagens/perfil");

		final Path raizProduto = Path.of(System.getProperty("user.home") + "/.shop/" + "imagens/produto");
		
		final Path raizPedido = Path.of(System.getProperty("user.home") + "/.shop/" + "imagens/pedido");
		
		try {
			if (!Files.exists(raizPerfil)){
				Files.createDirectories(raizPerfil);
			}
			if (!Files.exists(raizProduto)) {
				Files.createDirectories(raizProduto);
			}
			if (!Files.exists(raizPedido)) {
				Files.createDirectories(raizPedido);
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

}
