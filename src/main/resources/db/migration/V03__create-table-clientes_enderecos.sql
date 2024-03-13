CREATE TABLE clientes_enderecos (
	id BIGSERIAL,
	cep VARCHAR(8) NOT NULL,
	rua VARCHAR(200) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	complemento VARCHAR(255),
	bairro VARCHAR(100) NOT NULL,
	cidade VARCHAR(100) NOT NULL,
	estado CHAR(2) NOT NULL,
	endereco_padrao BOOLEAN NOT NULL,
	cliente_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (cliente_id)
	 REFERENCES clientes (id)
);