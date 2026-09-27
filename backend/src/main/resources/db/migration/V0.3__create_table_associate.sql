create extension if not exists "pgcrypto";

create table TB_ASSOCIATE(
        ID bigserial primary key,
        ID_EXTERNAL_UUID uuid not null default gen_random_uuid(),
        NAME VARCHAR(255) not null,
        EMAIL VARCHAR(255) not null,
        PASSWORD VARCHAR(255) not null,
        DOCUMENT_NUMBER VARCHAR(30) not null,
        CREATED_AT TIMESTAMP WITH TIME ZONE NOT NULL,
        UPDATED_AT TIMESTAMP WITH TIME ZONE,
        constraint tb_associate_external_uuid_unique unique (ID_EXTERNAL_UUID),
        constraint tb_associate_email_unique unique (EMAIL),
        constraint tb_associate_document_number_unique unique (DOCUMENT_NUMBER)
);

