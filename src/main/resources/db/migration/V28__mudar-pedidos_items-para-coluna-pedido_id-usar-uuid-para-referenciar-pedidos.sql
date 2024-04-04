ALTER TABLE pedidos_items DROP COLUMN pedido_id;

ALTER TABLE pedidos_items ADD COLUMN pedido_id UUID;

ALTER TABLE pedidos_items ADD CONSTRAINT pedidos_items_pedido_id_fkey FOREIGN KEY (pedido_id) REFERENCES pedidos(id);