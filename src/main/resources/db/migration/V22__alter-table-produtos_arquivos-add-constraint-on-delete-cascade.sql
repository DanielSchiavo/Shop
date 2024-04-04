ALTER TABLE produtos_arquivos DROP CONSTRAINT produtos_arquivos_produto_id_fkey;

ALTER TABLE produtos_arquivos
ADD CONSTRAINT produtos_arquivos_produto_id_fkey FOREIGN KEY (produto_id)
REFERENCES produtos (id) ON DELETE CASCADE;