create database parsers;

create table parsers.public.sites (
    site_id serial primary key,
    url varchar(100) not null,
    name varchar(250) not null,
    date_create timestamptz not null default now(),
    date_update timestamptz not null default now()
);
comment on column parsers.public.sites.url is 'Main site page';
comment on column parsers.public.sites.name is 'Site name';
comment on column parsers.public.sites.date_create is 'Date insert row';
comment on column parsers.public.sites.date_update is 'Date update row';

create table options (
    option_id serial primary key,
    name varchar(100) not null,
    description varchar(250),
    settings json not null
);
comment on column parsers.public.options.name is 'Parsing name';
comment on column parsers.public.options.description
    is 'Parsing short description';
comment on column parsers.public.options.settings
    is 'Settings to de used for parsing';

create table parsers.public.result_tables (
    result_table_id serial primary key,
    schema varchar(20) not null,
    name varchar(50) not null
);
comment on column parsers.public.result_tables.schema
    is 'Table schema which contains parsing results';
comment on column parsers.public.result_tables.name
    is 'Table name which contains parsing results';

create table parsers.public.result_tables_insert (
    result_table_id int4 primary key,
    insert_order varchar(250) not null,
    foreign key (result_table_id) references result_tables on delete cascade
);
comment on column parsers.public.result_tables_insert.insert_order
    is 'Order for insert data into parsing result table';

create table parsers.public.site_options (
    site_option_id serial primary key,
    site_id int4 not null,
    option_id int4 not null,
    result_table_id int4 not null,
    run boolean not null default false,
    foreign key (site_id) references sites on delete cascade,
    foreign key (option_id) references options on delete cascade,
    foreign key (result_table_id) references result_tables on delete cascade
);
comment on column parsers.public.site_options.run is 'Flag to parsing work';

create table parsers.public.reports (
    report_id serial primary key,
    site_operation_id int4 not null,
    parse_date timestamptz not null,
    massage text,
    success boolean not null,
    foreign key (site_operation_id) references site_options on delete cascade
);
comment on column parsers.public.reports.parse_date is 'Parsing date';
comment on column parsers.public.reports.massage is 'All parsing report';
comment on column parsers.public.reports.success is 'Parsing successful';
