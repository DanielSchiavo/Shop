package br.com.danielschiavo.shared.infra.insertdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public void clean() {
        String[] tables = new String[]{"files_references",
                                       "orders_payments",
                                       "orders_deliveries",
                                       "orders",
                                       "products_deliveries_types",
                                       "products_files",
                                       "products",
                                       "customers_cards",
                                       "customers_addresses",
                                       "customers_roles",
                                       "customers",
                                       "categories"};

        for (String table : tables) {
            if (tabelaExiste(table)) {
                jdbcTemplate.execute("DELETE FROM " + table + ";");

                String sequenceName = null;
//                if (table.contentEquals("clientes_carrinhos")) {
//                	sequenceName = jdbcTemplate.queryForObject(
//                			"SELECT pg_get_serial_sequence('" + table + "', 'cliente_id')", String.class);
//                } else {
//                	sequenceName = jdbcTemplate.queryForObject(
//                			"SELECT pg_get_serial_sequence('" + table + "_id_seq')", String.class);
//                }

                if (!table.contentEquals("files_references")){
                    sequenceName = jdbcTemplate.queryForObject(
                            "SELECT pg_get_serial_sequence('" + table + "', 'id')", String.class);
                }

                if(sequenceName != null) {
                    jdbcTemplate.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1;");
                }
            }
        }
    }

    private boolean tabelaExiste(String tabela) {
        String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, tabela);
    }

}