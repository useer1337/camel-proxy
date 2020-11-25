create sequence seq_temporary_token;

create table temporary_token(
    id numeric primary key default nextval('seq_temporary_token'),
    token varchar,
    patient_id varchar
);

comment on table generated_token is 'Таблицы токенов и PatientId';

comment on column generated_token.id is 'Идентификатор';
comment on column generated_token.token is 'Токен';
comment on column generated_token.patient_id is 'PatientID полученный от МИС';


create sequence seq_mis_address;

create table mis_address(
    id numeric primary key default nextval('seq_mis_address'),
    address_name varchar
);

comment on table mis_address is 'Таблицы адрессов мисов';

comment on column mis_address.id is 'Идентификатор';
comment on column mis_address.address_name is 'Адрес МИС';

create table temporary_token_mis_address(
    id_temporary_token numeric constraint fk_temporary_token references temporary_token(id),
    id_mis_address numeric constraint fk_mis_address references mis_address(id)
);

comment on table generated_token_mis_address is 'Промежуточная таблица между таблицей адрессов мисов и токенов и PatientId';

comment on column generated_token_mis_address.id_temporary_token is 'Идентификатор токенов и PatientId';
comment on column generated_token_mis_address.id_mis_address is 'Идентификатор таблицы адрессов';
