create database sessions;

create table sessions.public.sessions (
    session_id varchar(40) primary key,
    last_date_entry timestamptz not null default now()
);
comment on column sessions.public.sessions.last_date_entry is 'Last login time';

create table sessions.public.users (
    user_id serial primary key,
    name varchar(30) not null,
    token varchar(50) not null,
    create_date timestamptz not null default now()
);
comment on column sessions.public.users.name is 'Users name';
comment on column sessions.public.users.token is 'Users token';
comment on column sessions.public.users.create_date is 'Date creation user';

create table sessions.public.user_sessions (
    user_id int4,
    session_id varchar(40),
    foreign key (user_id) references sessions.public.users on delete cascade,
    foreign key (session_id)
        references sessions.public.sessions on delete cascade,
    primary key (user_id, session_id)
);
