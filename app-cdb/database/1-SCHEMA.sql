drop schema if exists `computer-database-db`;
  create schema if not exists `computer-database-db`;
  use `computer-database-db`;

  drop table if exists computer;
  drop table if exists company;
  drop table if exists user;
  drop table if exists role;

  create table company (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    constraint pk_company primary key (id))
  ;

  create table computer (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    introduced                timestamp NULL,
    discontinued              timestamp NULL,
    company_id                bigint default NULL,
    constraint pk_computer primary key (id))
  ;

  alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
  create index ix_computer_company_1 on computer (company_id);
  
  create  table user (
    id bigint NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL ,
    password VARCHAR(255) NOT NULL ,
    enabled TINYINT NOT NULL DEFAULT 1 ,
    constraint pk_user primary key (id))
  ;

  create table role (
    id bigint NOT NULL AUTO_INCREMENT,
    user_id bigint default NULL,
    role varchar(50) NOT NULL,
    constraint pk_role primary key (id))
  ;
  alter table role add constraint fk_role_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
  create index ix_role_user_1 on role (user_id);