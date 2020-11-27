create sequence seq_token;

create table token(
    id numeric primary key default nextval('seq_token'),
    value varchar,
    session varchar
);

comment on table token is 'Таблица токенов';

comment on column token.id is 'Идентификатор';
comment on column token.value is 'Значение токена';
comment on column token.session is 'Значение сесси к которой привязан токен';

create sequence seq_patient_id_data;

create table patient_id_data(
    id numeric primary key default nextval('seq_patient_id_data'),
    id_token numeric constraint fk_id_token references token(id),
    value varchar,
    address varchar
);

comment on table patient_id_data is 'Таблица с patientId и адресом';

comment on column patient_id_data.id is 'Идентификатор';
comment on column patient_id_data.value is 'Значение patientId';
comment on column patient_id_data.address is 'От кого пришел patientId';
comment on column patient_id_data.id_token is 'Токен';