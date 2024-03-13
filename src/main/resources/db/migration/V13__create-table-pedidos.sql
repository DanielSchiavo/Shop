CREATE TABLE pedidos (
	id BIGSERIAL,
	valor_total NUMERIC(8,2) NOT NULL,
	data_pedido TIMESTAMP NOT NULL,
	nome_cliente VARCHAR(255) NOT NULL,
	cpf CHAR(11) NOT NULL,
	status_pedido VARCHAR(100) NOT NULL,
	pagamento_id BIGINT NOT NULL,
	entrega_id BIGINT NOT NULL,
	cliente_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (pagamento_id)
	 REFERENCES pedidos_pagamento (id),
	FOREIGN KEY (entrega_id)
	 REFERENCES pedidos_entrega (id),
	FOREIGN KEY (cliente_id)
	 REFERENCES clientes (id)
);