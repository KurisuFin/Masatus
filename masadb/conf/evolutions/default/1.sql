# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table reference (
  id                        integer not null,
  cite_key                  varchar(255) not null,
  address                   varchar(255),
  author                    varchar(255) not null,
  edition                   varchar(255),
  year                      integer not null,
  title                     varchar(255) not null,
  publisher                 varchar(255) not null,
  volume                    integer,
  constraint pk_reference primary key (id))
;

create sequence reference_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists reference;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists reference_seq;

