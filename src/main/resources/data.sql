drop table if exists messages;

create table messages(
	id bigint auto_increment primary key,
	message varchar(100) not null,
	author varchar(50) not null
);

insert into messages(message, author) values ('message 1', 'initiation'),
											 ('message 2', 'initiation');