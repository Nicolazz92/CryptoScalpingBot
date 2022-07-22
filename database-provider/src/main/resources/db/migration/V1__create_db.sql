drop table if exists predictions cascade;
drop table if exists ratio_params cascade;
drop table if exists symbol_info_short cascade;
drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start 1 increment 1;

create table predictions
(
    id          int8    not null,
    can_buy     boolean not null,
    fresh_limit timestamp,
    primary key (id)
);

create table ratio_params
(
    id                    int8 not null,
    deals_count           int4,
    delta_minute_interval int4,
    delta_percent         float8,
    fresh_limit           timestamp,
    result_percent        float8,
    symbol                varchar(255),
    primary key (id)
);

create table symbol_info_short
(
    id                  int8         not null,
    base_asset          varchar(255),
    lot_size_min        float8       not null,
    market_lot_size_min float8       not null,
    symbol              varchar(255) not null,
    primary key (id)
);
