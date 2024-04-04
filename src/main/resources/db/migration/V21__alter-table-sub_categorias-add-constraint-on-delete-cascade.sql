ALTER TABLE sub_categorias DROP CONSTRAINT sub_categorias_categoria_id_fkey;

ALTER TABLE sub_categorias
ADD CONSTRAINT sub_categorias_categoria_id_fkey FOREIGN KEY (categoria_id)
REFERENCES categorias (id) ON DELETE CASCADE;