package br.com.danielschiavo.shop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
		
		final Path raizPerfil = Paths.get("imagens/perfil");

		final Path raizProduto = Paths.get("imagens/produto");
		
		final Path raizPedido = Paths.get("imagens/pedido");
		
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
