CREATE TABLE clientes_cartoes (
	id BIGSERIAL,
	nome_banco VARCHAR(255) NOT NULL,
	numero_cartao CHAR(16) NOT NULL,
	nome_no_cartao VARCHAR(255) NOT NULL,
	validade_cartao CHAR(5) NOT NULL,
	cartao_padrao BOOLEAN NOT NULL,
	tipo_cartao VARCHAR(15) CHECK (tipo_cartao IN ('CREDITO','DEBITO')) NOT NULL,
	cliente_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (cliente_id)
	 REFERENCES clientes (id)
);