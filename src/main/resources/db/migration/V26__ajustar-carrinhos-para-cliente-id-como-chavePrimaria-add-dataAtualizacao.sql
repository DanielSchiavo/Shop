ALTER TABLE carrinhos DROP COLUMN id CASCADE;

ALTER TABLE carrinhos DROP COLUMN cliente_id;

ALTER TABLE carrinhos ADD COLUMN id BIGINT NOT NULL;

ALTER TABLE carrinhos ADD PRIMARY KEY (id);

ALTER TABLE carrinhos ADD COLUMN data_atualizacao TIMESTAMP;

ALTER TABLE carrinhos ADD CONSTRAINT carrinhos_cliente_id_fkey FOREIGN KEY (id) REFERENCES clientes (id) ON DELETE CASCADE;