CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS endpoints (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    url         VARCHAR(500) NOT NULL,
    owner_email VARCHAR(255) NOT NULL,
    active      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS health_checks (
    id            BIGSERIAL PRIMARY KEY,
    endpoint_id   BIGINT REFERENCES endpoints(id) ON DELETE CASCADE,
    status        VARCHAR(10) NOT NULL,
    status_code   INTEGER,
    response_ms   INTEGER,
    checked_at    TIMESTAMP DEFAULT NOW()
);