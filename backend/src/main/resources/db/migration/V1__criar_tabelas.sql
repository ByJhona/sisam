create table tipo(
    id_tipo serial not null,
    descricao text not null unique,

    primary key(id_tipo)
);


create table indice(
    id serial not null,
    data  date not null,
    valor NUMERIC(32, 15) not null,
    id_tipo int not null,
    unique(data, id_tipo),

    primary key(id),
    foreign key (id_tipo) references tipo(id_tipo)

);


