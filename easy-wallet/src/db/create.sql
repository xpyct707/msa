CREATE TABLE IF NOT EXISTS public.wallet (
    id BIGSERIAL,
    user_id VARCHAR(100) NOT NULL,
    balance BIGINT NOT NULL
);