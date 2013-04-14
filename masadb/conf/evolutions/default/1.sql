# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table reference (
  id                        integer not null,
  type                      integer not null,
  cite_key                  varchar(255) not null,
  title                     varchar(255) not null,
  author                    varchar(255) not null,
  year                      integer not null,
  month                     varchar(255),
  volume                    integer,
  number                    integer,
  edition                   varchar(255),
  pages                     varchar(255),
  book_title                varchar(255),
  publisher                 varchar(255),
  address                   varchar(255),
  organization              varchar(255),
  constraint ck_reference_type check (type in (0,1,2,3,4,5,6,7,8,9,10,11,12)),
  constraint pk_reference primary key (id))
;

create sequence reference_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists reference;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists reference_seq;

