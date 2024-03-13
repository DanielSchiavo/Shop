CREATE TABLE pedidos_entrega (
	id BIGSERIAL,
	tipo_entrega VARCHAR(50) NOT NULL,
	cep VARCHAR(8) NOT NULL,
	rua VARCHAR(200) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	complemento VARCHAR(255),
	bairro VARCHAR(100) NOT NULL,
	cidade VARCHAR(100) NOT NULL,
	estado CHAR(2) NOT NULL,
	PRIMARY KEY(id)
);