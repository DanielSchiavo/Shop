ALTER TABLE clientes_enderecos DROP CONSTRAINT clientes_enderecos_cliente_id_fkey;

ALTER TABLE clientes_enderecos
ADD CONSTRAINT clientes_enderecos_cliente_id_fkey FOREIGN KEY (cliente_id)
REFERENCES clientes (id) ON DELETE CASCADE;