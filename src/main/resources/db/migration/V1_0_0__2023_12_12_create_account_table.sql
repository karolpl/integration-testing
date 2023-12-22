CREATE TABLE account (
	id UUID PRIMARY KEY,
	user_id UUID NOT NULL,
	name VARCHAR(255) NOT NULL,
	balance NUMERIC(17,2) NOT NULL,
	currency VARCHAR(3) NOT NULL,
	description VARCHAR(255),
	created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	CONSTRAINT account_name_user_id_unique_key UNIQUE (name, user_id)
);
