CREATE TABLE order_status(
    id SMALLINT GENERATED ALWAYS AS IDENTITY,
    status VARCHAR(30) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

INSERT INTO order_status (status)
VALUES
('PROCESSING'),
('SHIPPED'),
('DELIVERED'),
('CANCELLED')
