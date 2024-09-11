create table indices(
    id serial not null,
    data  date not null unique,
    valor real not null,



    primary key(id)
    );