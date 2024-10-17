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
create table tipo(
    id_tipo serial not null,
    descricao text not null unique,

    primary key(id_tipo)
);

create table indices(
    id serial not null,
    data  date not null,
    valor NUMERIC(32, 15) not null,
    id_tipo int not null,
    unique(data, id_tipo),

    primary key(id),
    foreign key (id_tipo) references tipo(id_tipo)

);


