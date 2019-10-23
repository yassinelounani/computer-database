drop schema if exists `computer-database-db`;
  create schema if not exists `computer-database-db`;
  use `computer-database-db`;

  drop table if exists computer;
  drop table if exists company;

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
  
  

insert into company (id,name) values (  1,'Apple Inc.');
insert into company (id,name) values (  2,'Thinking Machines');
insert into company (id,name) values (  3,'RCA');
insert into company (id,name) values (  4,'Netronics');
insert into company (id,name) values (  5,'Tandy Corporation');
insert into company (id,name) values (  6,'Commodore International');
insert into company (id,name) values (  7,'MOS Technology');
insert into company (id,name) values (  8,'Micro Instrumentation and Telemetry Systems');
insert into company (id,name) values (  9,'IMS Associates, Inc.');
insert into company (id,name) values ( 10,'Digital Equipment Corporation');

insert into computer (id,name,introduced,discontinued,company_id) values (  1,'MacBook Pro 15.4 inch',null,null,1);
insert into computer (id,name,introduced,discontinued,company_id) values (  2,'CM-2a',null,null,2);
insert into computer (id,name,introduced,discontinued,company_id) values (  3,'CM-200',null,null,2);
insert into computer (id,name,introduced,discontinued,company_id) values (  4,'CM-5e',null,null,2);
insert into computer (id,name,introduced,discontinued,company_id) values (  5,'CM-5','1991-01-01',null,2);
insert into computer (id,name,introduced,discontinued,company_id) values ( 6,'Apple II','1977-04-01','1993-10-01',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 7,'Apple III Plus','1983-12-01','1984-04-01',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 8,'COSMAC ELF',null,null,3);
insert into computer (id,name,introduced,discontinued,company_id) values ( 9,'COSMAC VIP','1977-01-01',null,3);
insert into computer (id,name,introduced,discontinued,company_id) values ( 10,'ELF II','1977-01-01',null,4);
insert into computer (id,name,introduced,discontinued,company_id) values ( 11,'Macintosh','1984-01-24',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 12,'Macintosh II',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( 13,'Macintosh Plus','1986-01-16','1990-10-15',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 14,'Macintosh IIfx',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( 15,'iMac','1998-01-01',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 16,'Mac Mini','2005-01-22',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 17,'Mac Pro','2006-08-07',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 18,'Apple Lisa',null,null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( 19,'CM-2',null,null,2);
insert into computer (id,name,introduced,discontinued,company_id) values ( 20,'Connection Machine','1987-01-01',null,2);  
