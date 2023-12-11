drop table example1;
create table example1
(
    id          bigint primary key,
    name        varchar(255),
    age         int,
    birth       datetime,
    die         tinyint  default 0,
    status      tinyint  default 1,
    update_by   varchar(255),
    update_time datetime default now(),
    create_by   varchar(255),
    create_time datetime default now(),
    deleted     tinyint  default 0
)