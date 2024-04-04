ALTER TABLE clientes_cartoes DROP CONSTRAINT clientes_cartoes_cliente_id_fkey;

ALTER TABLE clientes_cartoes
ADD CONSTRAINT clientes_cartoes_cliente_id_fkey FOREIGN KEY (cliente_id)
REFERENCES clientes (id) ON DELETE CASCADE;