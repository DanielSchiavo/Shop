package br.com.danielschiavo.shop.infra;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Profile("dev")
@Component
public class LimpadorBancoDeDados {

    private final JdbcTemplate jdbcTemplate;

    public LimpadorBancoDeDados(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void cleanupDatabase() {
        String[] tables = new String[]{"pedidos_items", "pedidos", "pedidos_entrega", "pedidos_pagamento", "carrinhos_items", "carrinhos", "produtos_tipo_entrega", "produtos_arquivos", "produtos", "sub_categorias", "categorias", "clientes_enderecos", "clientes_cartoes", "clientes"};
        
        for (String table : tables) {
            jdbcTemplate.execute("DELETE FROM " + table + ";");

            String sequenceName = jdbcTemplate.queryForObject(
                "SELECT pg_get_serial_sequence('" + table + "', 'id')", String.class);

            if(sequenceName != null) {
                jdbcTemplate.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1;");
            }
        }
    }

}