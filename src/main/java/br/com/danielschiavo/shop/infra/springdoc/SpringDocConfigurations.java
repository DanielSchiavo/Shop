package br.com.danielschiavo.shop.infra.springdoc;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SpringDocConfigurations implements OpenApiCustomizer {

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
    
    @Override
    public void customise(OpenAPI openApi) {
       List<Tag> sortedTags = openApi.getTags()
                .stream()
                .sorted(Comparator.comparing(tag -> tag.getName().substring(0, 2)))
                .collect(Collectors.toList());
       openApi.setTags(sortedTags);
    }
}
