# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table entry (
  id                        bigint not null,
  data                      varchar(255),
  constraint pk_entry primary key (id))
;

create sequence entry_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists entry;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists entry_seq;

