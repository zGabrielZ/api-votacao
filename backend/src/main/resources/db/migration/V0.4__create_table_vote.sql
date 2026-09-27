create extension if not exists "pgcrypto";

create table TB_VOTE(
        ID bigserial primary key,
        ID_EXTERNAL_UUID uuid not null default gen_random_uuid(),
        ID_VOTING_SESSION bigint not null,
        ID_ASSOCIATE bigint not null,
        VOTE_OPTION varchar(10) not null,
        CREATED_AT TIMESTAMP WITH TIME ZONE NOT NULL,
        UPDATED_AT TIMESTAMP WITH TIME ZONE,
        constraint tb_vote_external_uuid_unique unique (ID_EXTERNAL_UUID),
        constraint tb_vote_voting_session_unique unique (ID_VOTING_SESSION, ID_ASSOCIATE),
        constraint tb_vote_vote_option_check check (VOTE_OPTION in ('YES', 'NO'))
);

alter table TB_VOTE add foreign key (ID_VOTING_SESSION) references TB_VOTING_SESSION(ID);
alter table TB_VOTE add foreign key (ID_ASSOCIATE) references TB_ASSOCIATE(ID);
