CREATE TABLE categories (
	id SERIAL,
	name VARCHAR(100),
	PRIMARY KEY (id)
);

CREATE TABLE sub_categories (
	id SERIAL,
	name VARCHAR(100),
	category_id INT,
	PRIMARY KEY (id),
	CONSTRAINT fk_category
		FOREIGN KEY (category_id)
			REFERENCES categories(id)
				ON DELETE CASCADE
				ON UPDATE CASCADE
);

CREATE TABLE products (
	id SERIAL,
	name VARCHAR(255) NOT NULL,
	description TEXT NOT NULL,
	price NUMERIC(8,2),
	quantity INT,
	active BOOLEAN,
	sub_category_id INT,
	PRIMARY KEY (id),
	CONSTRAINT fk_sub_category
		FOREIGN KEY (sub_category_id)
			REFERENCES sub_categories(id)
				ON DELETE CASCADE
				ON UPDATE CASCADE
);