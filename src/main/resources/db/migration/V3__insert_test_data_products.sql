CREATE TABLE IF NOT EXISTS products
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    product_name     VARCHAR(255)                            NOT NULL,
    purchase_price   DECIMAL                                 NOT NULL,
    sale_price       DECIMAL                                 NOT NULL,
    product_group_id BIGINT                                  NOT NULL,
    article          VARCHAR(255)                            NOT NULL,
    created          TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated          TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

INSERT INTO products (product_name, purchase_price, sale_price, article,
                      product_group_id)
VALUES ('Product 1', 100.00, 120.00, 'Article 1', 1),
       ('Product 2', 50.00, 60.00, 'Article 2', 2),
       ('Product 3', 200.00, 240.00, 'Article 3', 3),
       ('Product 4', 80.00, 96.00, 'Article 4', 3),
       ('Product 5', 250.00, 300.00, 'Article 5', 2);