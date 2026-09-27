create extension if not exists "pgcrypto";

create table TB_VOTING_SESSION(
        ID bigserial primary key,
        ID_EXTERNAL_UUID uuid not null default gen_random_uuid(),
        ID_AGENDA bigint not null,
        VOTING_START_TIME TIMESTAMP WITH TIME ZONE NOT NULL,
        VOTING_END_TIME TIMESTAMP WITH TIME ZONE NOT NULL,
        VOTING_STATUS varchar(10) not null,
        CREATED_AT TIMESTAMP WITH TIME ZONE NOT NULL,
        UPDATED_AT TIMESTAMP WITH TIME ZONE,
        constraint tb_voting_session_external_uuid_unique unique (ID_EXTERNAL_UUID),
        constraint tb_voting_session_status_ck check (VOTING_STATUS in ('OPEN', 'CLOSED'))
);

alter table TB_VOTING_SESSION add foreign key (ID_AGENDA) references TB_AGENDA(ID);

