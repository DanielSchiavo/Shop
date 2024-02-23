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
		
		final Path rootProfile = Paths.get("images/profile");
		final Path rootProduct = Paths.get("images/product");
		
		try {
			if (!Files.exists(rootProfile)) {
				Files.createDirectories(rootProfile);
			}
			if (!Files.exists(rootProduct)) {
				Files.createDirectories(rootProduct);
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
