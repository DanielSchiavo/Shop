CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE pedidos ADD COLUMN nova_coluna_uuid UUID;

ALTER TABLE pedidos DROP COLUMN id CASCADE;

ALTER TABLE pedidos RENAME COLUMN nova_coluna_uuid TO id;

ALTER TABLE pedidos ALTER COLUMN id SET DEFAULT uuid_generate_v4();

ALTER TABLE pedidos ADD PRIMARY KEY (id);