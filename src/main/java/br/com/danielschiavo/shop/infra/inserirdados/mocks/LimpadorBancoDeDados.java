package br.com.danielschiavo.shop.infra.inserirdados.mocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LimpadorBancoDeDados {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public void limpar() {
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