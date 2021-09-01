-- https://r2dbc.io/spec/0.8.0.M8/spec/html/#datatypes.mapping
-- https://www.h2database.com/html/datatypes.html#bigint_type
-- https://www.sqlines.com/postgresql/datatypes/serial
-- https://github.com/spring-projects/spring-data-r2dbc/blob/fe7308100a2d06401fa03eaf3722d5c0e3ad514b/src/main/asciidoc/reference/r2dbc-repositories.adoc

-- https://x-team.com/blog/automatic-timestamps-with-postgresql/
-- update_at trigger
CREATE OR REPLACE FUNCTION update_timestamp() RETURNS TRIGGER AS '
BEGIN
    NEW.temp_lastupdated = now();
RETURN NEW;
END;
' language plpgsql;

-- create unique index concurrently if not exists idx_users_snowflake ON users (snowflake);
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    snowflake BIGINT NOT NULL UNIQUE,
    wallet BIGINT NOT NUlL CONSTRAINT non_negative_wallet CHECK (wallet >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    version BIGINT NOT NULL
);

-- trigger
CREATE TRIGGER set_timestamp
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE PROCEDURE update_timestamp();