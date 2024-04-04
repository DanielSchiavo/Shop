ALTER TABLE carrinhos_items DROP CONSTRAINT carrinhos_items_carrinho_id_fkey;

ALTER TABLE carrinhos_items DROP CONSTRAINT carrinhos_items_produto_id_fkey;

ALTER TABLE carrinhos_items
ADD CONSTRAINT carrinhos_items_carrinho_id_fkey FOREIGN KEY (carrinho_id)
REFERENCES carrinhos (id) ON DELETE CASCADE,
ADD CONSTRAINT carrinhos_items_produto_id_fkey FOREIGN KEY (produto_id)
REFERENCES produtos (id) ON DELETE CASCADE;