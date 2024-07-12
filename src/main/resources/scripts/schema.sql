CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

GRANT ALL PRIVILEGES ON DATABASE authorizer TO authorizer;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO authorizer;

CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    amount NUMERIC NOT NULL,
    account_id VARCHAR(255) NOT NULL,
    credit_type VARCHAR(255) NOT NULL,
    merchant TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account_balance(
    id SERIAL PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    total_amount NUMERIC NOT NULL,
    type VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX account_balance_account_id_type_idx ON account_balance(account_id, type);

INSERT INTO account_balance (account_id, total_amount, type) VALUES ('1', 1000, 'CASH');
INSERT INTO account_balance (account_id, total_amount, type) VALUES ('1', 1000, 'SUPERMARKET');
INSERT INTO account_balance (account_id, total_amount, type) VALUES ('1', 1000, 'RESTAURANT');
INSERT INTO account_balance (account_id, total_amount, type) VALUES ('1', 0, 'MOBILITY');

INSERT INTO account_balance (account_id, total_amount, type) VALUES ('2', 0, 'CASH');
INSERT INTO account_balance (account_id, total_amount, type) VALUES ('2', 0, 'SUPERMARKET');
INSERT INTO account_balance (account_id, total_amount, type) VALUES ('2', 1000, 'RESTAURANT');
INSERT INTO account_balance (account_id, total_amount, type) VALUES ('2', 1000, 'MOBILITY');

