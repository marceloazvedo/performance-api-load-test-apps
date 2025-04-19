CREATE TABLE IF NOT EXISTS customer_entity (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    tax_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_entity (
    table_id SERIAL PRIMARY KEY,
    id VARCHAR(255) NOT NULL,
    reference_id VARCHAR(255) NOT NULL,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customer_entity(id)
);

CREATE INDEX IF NOT EXISTS idx_order_entity_order_id ON order_entity (id);
CREATE INDEX IF NOT EXISTS idx_order_entity_reference_id ON order_entity (reference_id);

CREATE TABLE IF NOT EXISTS item_entity (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    value INT NOT NULL,
    quantity INT NOT NULL,
    of_order BIGINT NOT NULL,
    FOREIGN KEY (of_order) REFERENCES order_entity(table_id)
);
