-- https://r2dbc.io/spec/0.8.0.M8/spec/html/#datatypes.mapping
-- https://www.h2database.com/html/datatypes.html#bigint_type
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    wallet BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT
);