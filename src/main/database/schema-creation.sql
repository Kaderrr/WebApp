DROP SCHEMA IF EXISTS naklaken CASCADE;

-- Create the naklaken schema
CREATE SCHEMA naklaken;
COMMENT ON SCHEMA naklaken IS 'Schema for containing the objects of the user naklaken';

/*GUEST TABLE*/
CREATE TYPE guest_type AS ENUM ('normal', 'disabled', 'premium');
CREATE TABLE IF NOT EXISTS naklaken.Guest
(
    id      varchar(255) NOT NULL,
    name    VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    type    guest_type   NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE naklaken.Guest IS 'Represents a guest';
COMMENT ON COLUMN naklaken.Guest.id IS 'The unique id of a guest.';
COMMENT ON COLUMN naklaken.Guest.surname IS 'The surname of the guest.';
COMMENT ON COLUMN naklaken.Guest.type IS 'The type of the employee.';

/*USER TABLE*/
CREATE TYPE permission_level AS ENUM ('service_provider', 'receptionist', 'admin');
CREATE TABLE IF NOT EXISTS naklaken.User
(
    id               varchar(255)     NOT NULL,
    email            VARCHAR(255)   unique  NOT NULL,
    password         VARCHAR(255)     NOT NULL,
    active           BOOLEAN          NOT NULL DEFAULT true,
    permission_level permission_level NOT NULL,
    PRIMARY KEY (id)
);

/*BOOKING TABLE*/
create table if not exists booking
(
    id        serial
        constraint booking_pk
            primary key,
    check_in  timestamp            not null,
    check_out timestamp            not null,
    room      varchar         not null,
    balance   float default 0 not null,
    "guest_id" varchar         not null
        constraint booking_guest_fk
            references guest
            on update cascade on delete cascade,
    "user_id"  varchar         not null
        constraint booking_user_fk
            references "user"
            on update cascade on delete cascade
);

create unique index booking_id_uindex
    on booking (id);

/*TRANSACTION TABLE*/
CREATE TYPE transaction_type AS ENUM ('deposit', 'payment');
create table transactions
(
    id          serial
        constraint transactions_pk
            primary key,
    state       boolean          not null,
    amount      float            not null,
    description varchar,
    datetime    timestamp             not null,
    type        transaction_type not null,
    "user_id"    varchar          not null
        constraint transactions_user_fk
            references "user"
            on update cascade on delete cascade,
    "booking_id" int              not null
        constraint transactions_booking_fk
            references booking
            on update cascade on delete cascade
);

create unique index transactions_id_uindex
    on transactions (id);

/*RFID TABLE*/
CREATE TYPE rfid_type AS ENUM ('band', 'card');
CREATE TYPE rfid_status AS ENUM ('valid', 'lost', 'broken');
CREATE TABLE IF NOT EXISTS naklaken.Rfid
(
    code   varchar(255) NOT NULL,
    type   rfid_type    NOT NULL,
    status rfid_status  NOT NULL,
    PRIMARY KEY (code)
);

/*Match TABLE (code of rfid and booking id as foreign keys. Id as primary key) */
create table match
(
    id          serial
        constraint match_pk
            primary key,
    status      boolean not null,
    datetime    timestamp    not null,
    "booking_id" int     not null
        constraint match_booking_fk
            references booking
            on update cascade on delete cascade,
    "code_rfid"  varchar not null
        constraint match_code_fk
            references rfid
            on update cascade on delete cascade
);

create unique index match_id_uindex
    on match (id);








