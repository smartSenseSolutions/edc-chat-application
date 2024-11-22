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

--changeset Bhautik:2
CREATE TABLE edc_process_states (
    id SERIAL PRIMARY KEY,
    receiver_bpn VARCHAR(20) NOT NULL,
    offer_id VARCHAR(255),
    negotiation_id VARCHAR(255),
    agreement_id VARCHAR(255),
    transfer_id VARCHAR(255),
    error_detail varchar(255),
    created_at timestamp(6)     NULL     DEFAULT NOW(),
    updated_at timestamp(6)     NULL     DEFAULT NOW()
);

--changeset Bhautik:3
CREATE TABLE chat_messages (
    id SERIAL PRIMARY KEY,
    edc_process_state_id INT,
    partner_bpn VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    self_owner BOOLEAN,
    created_at timestamp(6)     NULL     DEFAULT NOW(),
    CONSTRAINT fk_edc_process_state FOREIGN KEY (edc_process_state_id) REFERENCES edc_process_states(id)
);

--changeset Bhautik:4
ALTER TABLE chat_messages ADD COLUMN IF NOT EXISTS  is_chat_success BOOLEAN;

--changeset Bhautik:5
ALTER TABLE edc_process_states ALTER COLUMN error_detail TYPE TEXT;

