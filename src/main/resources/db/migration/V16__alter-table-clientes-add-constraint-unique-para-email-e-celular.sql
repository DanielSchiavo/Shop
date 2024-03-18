ALTER TABLE clientes
ADD CONSTRAINT email_unique UNIQUE (email);

ALTER TABLE clientes
ADD CONSTRAINT celular_unique UNIQUE (celular);