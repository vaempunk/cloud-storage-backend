-- liquibase formatted sql

-- changeset vaem:1
CREATE TABLE module (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE,
    description VARCHAR(128) NOT NULL,
    date_created TIMESTAMP NOT NULL
);
CREATE TABLE container (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    module_id INT NOT NULL,
    parent_id INT,
    FOREIGN KEY(module_id) REFERENCES module(id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    FOREIGN KEY(parent_id) REFERENCES container(id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    UNIQUE(name, module_id, parent_id)
);
CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    size BIGINT NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_changed TIMESTAMP NOT NULL,
    parent_id INT NOT NULL,
    FOREIGN KEY(parent_id) REFERENCES container(id)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    UNIQUE(name, parent_id)
);