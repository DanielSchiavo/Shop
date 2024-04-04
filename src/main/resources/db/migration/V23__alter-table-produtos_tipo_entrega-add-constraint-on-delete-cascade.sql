ALTER TABLE produtos_tipo_entrega DROP CONSTRAINT produtos_tipo_entrega_produto_id_fkey;

ALTER TABLE produtos_tipo_entrega
ADD CONSTRAINT produtos_tipo_entrega_produto_id_fkey FOREIGN KEY (produto_id)
REFERENCES produtos (id) ON DELETE CASCADE;