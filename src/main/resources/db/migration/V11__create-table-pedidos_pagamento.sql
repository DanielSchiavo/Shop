CREATE TABLE pedidos_pagamento (
	id BIGSERIAL,
	metodo_pagamento VARCHAR(50) NOT NULL,
	status_pagamento VARCHAR(50) NOT NULL,
	data_pagamento TIMESTAMP,
	nome_banco VARCHAR(255) NOT NULL,
	numero_cartao CHAR(16) NOT NULL,
	nome_no_cartao VARCHAR(255) NOT NULL,
	validade_cartao CHAR(5) NOT NULL,
	numero_de_parcelas VARCHAR(2) NOT NULL,
	tipo_cartao VARCHAR(15) CHECK (tipo_cartao IN ('CREDITO','DEBITO')) NOT NULL,
	PRIMARY KEY (id)
);	