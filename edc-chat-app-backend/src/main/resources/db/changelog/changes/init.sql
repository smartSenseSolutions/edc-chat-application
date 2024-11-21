--liquibase formatted sql

--changeset Bhautik:1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE business_partner
(
    id         UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name       varchar(100)     NOT NULL,
    bpn        varchar(50)      NOT NULL,
    edc_url    varchar(250)     NOT NULL,
    created_at timestamp(6)     NULL     DEFAULT NOW(),
    updated_at timestamp(6)     NULL     DEFAULT NOW()
);
