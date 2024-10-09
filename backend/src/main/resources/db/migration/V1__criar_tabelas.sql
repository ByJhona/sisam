create table selic_acumulada(
    id serial not null,
    data  date not null unique,
    valor NUMERIC(10, 6) not null,

    primary key(id)
);

create table selic_mes(
    id serial not null,
    data  date not null unique,
    valor NUMERIC(10, 6) not null,

    primary key(id)
    );

create table fatores_atualizacao(
        id serial not null,
        data  date not null unique,
        valor NUMERIC(18, 15) not null,

        primary key(id)
);

create table fatores_indices(
    id serial not null,
                data  date not null unique,
                valor NUMERIC(10, 5) not null,

                primary key(id)
);
