drop table if exists hibernate_sequence;
drop table if exists predictions;
drop table if exists ratio_params;
drop table if exists symbol_info_short;

create table hibernate_sequence
(
    next_val bigint
);

insert into hibernate_sequence
values (1);

create table predictions
(
    id          bigint not null,
    can_buy     bit    not null,
    fresh_limit datetime(6),
    primary key (id)
);

create table ratio_params
(
    id                    bigint not null,
    deals_count           integer,
    delta_minute_interval integer,
    delta_percent         DOUBLE precision,
    fresh_limit           datetime(6),
    result_percent        double precision,
    symbol                varchar(255),
    primary key (id)
);

create table symbol_info_short
(
    id                  bigint           not null,
    base_asset          varchar(255),
    lot_size_min        double precision not null,
    market_lot_size_min double precision not null,
    symbol              varchar(255),
    primary key (id)
);

CREATE TABLE coin_info
(
    id                  BIGINT       NOT NULL,
    coin                VARCHAR(255) NULL,
    deposit_all_enable  BIT(1)       NOT NULL,
    withdraw_all_enable BIT(1)       NOT NULL,
    name                VARCHAR(255) NULL,
    free                VARCHAR(255) NULL,
    `locked`            VARCHAR(255) NULL,
    freeze              VARCHAR(255) NULL,
    withdrawing         VARCHAR(255) NULL,
    ipoing              VARCHAR(255) NULL,
    ipoable             VARCHAR(255) NULL,
    storage             VARCHAR(255) NULL,
    is_legal_money      BIT(1)       NOT NULL,
    trading             BIT(1)       NOT NULL,
    CONSTRAINT pk_coininfo PRIMARY KEY (id)
);

CREATE TABLE coin_info_network_list
(
    coin_info_id    BIGINT NOT NULL,
    network_list_id BIGINT NOT NULL
);

ALTER TABLE coin_info_network_list
    ADD CONSTRAINT uc_coin_info_network_list_networklist UNIQUE (network_list_id);

ALTER TABLE coin_info_network_list
    ADD CONSTRAINT fk_coiinfnetlis_on_coin_info FOREIGN KEY (coin_info_id) REFERENCES coin_info (id);

CREATE TABLE network_list
(
    id                        BIGINT       NOT NULL,
    network                   VARCHAR(255) NULL,
    coin                      VARCHAR(255) NULL,
    withdraw_integer_multiple VARCHAR(255) NULL,
    is_default                BIT(1)       NOT NULL,
    deposit_enable            BIT(1)       NOT NULL,
    withdraw_enable           BIT(1)       NOT NULL,
    deposit_desc              VARCHAR(255) NULL,
    withdraw_desc             VARCHAR(255) NULL,
    special_tips              VARCHAR(255) NULL,
    special_withdraw_tips     VARCHAR(255) NULL,
    name                      VARCHAR(255) NULL,
    reset_address_status      BIT(1)       NOT NULL,
    address_regex             VARCHAR(255) NULL,
    address_rule              VARCHAR(255) NULL,
    memo_regex                VARCHAR(255) NULL,
    withdraw_fee              VARCHAR(255) NULL,
    withdraw_min              VARCHAR(255) NULL,
    withdraw_max              VARCHAR(255) NULL,
    min_confirm               INT          NOT NULL,
    un_lock_confirm           INT          NOT NULL,
    same_address              BIT(1)       NOT NULL,
    estimated_arrival_time    INT          NOT NULL,
    deposit_dust              VARCHAR(255) NULL,
    CONSTRAINT pk_networklist PRIMARY KEY (id)
);

ALTER TABLE coin_info_network_list
    ADD CONSTRAINT fk_coiinfnetlis_on_network_list FOREIGN KEY (network_list_id) REFERENCES network_list (id);