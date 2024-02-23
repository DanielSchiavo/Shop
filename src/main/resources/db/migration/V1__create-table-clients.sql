CREATE TABLE clients (
	id SERIAL,
	cpf CHAR(11) NOT NULL UNIQUE,
	name VARCHAR(50) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	birth_date DATE NOT NULL,
	email VARCHAR(100) NOT NULL,
	password VARCHAR(100) NOT NULL,
	telephone CHAR(11),
	image VARCHAR(100),
	cep VARCHAR(10),
	street VARCHAR(100),
	number VARCHAR(10),
	complement VARCHAR(100),
	district VARCHAR(50),
	uf CHAR(2),
	PRIMARY KEY(id)
)