CREATE SCHEMA IF NOT EXISTS mini_wallet;

CREATE TABLE mini_wallet.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_xid UUID NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE mini_wallet.wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    balance NUMERIC NOT NULL default 0,
    enabled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL default now(),
    last_updated_at TIMESTAMP NOT NULL default now(),
    FOREIGN KEY (user_id) REFERENCES mini_wallet.users(id)
);

CREATE TABLE mini_wallet.transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference_id UUID unique not null,
    transaction_type VARCHAR(20) not null,
    status VARCHAR(20) not null,
    user_id UUID NOT NULL,
    wallet_id UUID NOT NULL,
    amount NUMERIC NOT NULL,
    created_at TIMESTAMP NOT NULL default now(),
    last_updated_at TIMESTAMP NOT NULL default now(),
    FOREIGN KEY (user_id) REFERENCES mini_wallet.users(id),
    FOREIGN KEY (wallet_id) REFERENCES mini_wallet.wallets(id)
);

CREATE INDEX idx_wallet_user ON mini_wallet.transactions (user_id);
