CREATE TABLE roles(
    id SMALLINT GENERATED BY DEFAULT AS IDENTITY,
    role VARCHAR(20) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO roles(role)
VALUES ('CUSTOMER'),
       ('EMPLOYEE'),
       ('ADMIN');
