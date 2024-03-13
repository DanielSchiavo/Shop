CREATE TABLE produtos_arquivos (
	id BIGSERIAL,
	nome VARCHAR(50),
	posicao SMALLINT,
	produto_id BIGINT,
	PRIMARY KEY (id),
	FOREIGN KEY (produto_id)
 	 REFERENCES produtos (id)
);