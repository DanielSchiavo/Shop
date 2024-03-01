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
		
		final Path rootPerfil = Paths.get("imagens/perfil");
		final Path rootProduto = Paths.get("imagens/produto");
		final Path rootPedido = Paths.get("imagens/pedido");
		
		try {
			if (!Files.exists(rootPerfil)) {
				Files.createDirectories(rootPerfil);
			}
			if (!Files.exists(rootProduto)) {
				Files.createDirectories(rootProduto);
			}
			if (!Files.exists(rootPedido)) {
				Files.createDirectories(rootPedido);
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

}
