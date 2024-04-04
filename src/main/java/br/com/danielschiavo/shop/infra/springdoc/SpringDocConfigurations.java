package br.com.danielschiavo.shop.infra.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfigurations {

    @Bean
    OpenAPI customOpenAPI() {
	   return new OpenAPI()
	          .components(new Components()
	          .addSecuritySchemes("bearer-key",
	          new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
              .info(new Info()
                      .title("Loja API")
                      .description("API Rest de uma aplicação de loja online, contendo basicamente todas as funcionalidades que uma loja precisa, que é um CRUD de Produto, Categoria, SubCategoria, Carrinho, Pedido, Usuario, Enderecos do usuario, Cartoes do usuario"));
	}
}
