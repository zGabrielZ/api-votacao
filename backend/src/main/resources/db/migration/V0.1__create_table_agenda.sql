create extension if not exists "pgcrypto";

create table TB_AGENDA(
        ID bigserial primary key,
        ID_EXTERNAL_UUID uuid not null default gen_random_uuid(),
        TITLE varchar(255) not null,
        DESCRIPTION varchar(255),
        CREATED_AT TIMESTAMP WITH TIME ZONE NOT NULL,
        UPDATED_AT TIMESTAMP WITH TIME ZONE,
        constraint tb_agenda_external_uuid_unique unique (ID_EXTERNAL_UUID)
);
