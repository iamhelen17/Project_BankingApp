create schema bago_national_bank;

create table bago_national_bank.customer (
	customer_id			serial			not null,
	first_name			varchar(100)	not null,
	last_name			varchar(100)	not null,
	gender				varchar(1)		not null,
	dob					date			not null,
	address1			varchar(50)		not null,
	address2			varchar(50)			null,
	city				varchar(20)		not null,
	state				varchar(4)		not null,
	zip5				varchar(5)		not null,
	zip4				varchar(4)			null,
	phone1				varchar(15)		not null,
	phone2				varchar(15)		not null,
	email				varchar(100)	not null,
	join_date			date			not null,
	primary key (customer_id)
);

insert into bago_national_bank.customer (first_name, last_name, gender, dob, address1, address2, city, state, zip5, zip4, phone1, phone2, email, join_date)
values ('Jane', 'Warner', 'F', '1998-08-20', '1822 S Val Vista Dr', 'Suite 106', 'Mesa', 'AZ', 85204, null, '1237648904', '1736540983', 'jhrf@gmail.com', '2019-01-19')
;

insert into bago_national_bank.customer (first_name, last_name, gender, dob, address1, address2, city, state, zip5, zip4, phone1, phone2, email, join_date)
values ('Bob', 'Dillon', 'M', '1988-12-23', '6505 E Southern Ave', 'Ste 202', 'Mesa', 'AZ', 85206, null, '1384950564', '1473893645', 'bmda@gmail.com', '2020-01-10')
;


create table bago_national_bank.account (
	account_id			serial			not null,
	account_type		varchar(15)		not null,
	balance				float8			not null,
	opened_date			date			not null,
	closed_date			date				null,
	status				varchar(15)		not null,
	approved_by			int					null,
	customer_id			int				not null,
	primary key (account_id),
	constraint account_fk
	foreign key (customer_id)
	references bago_national_bank.customer (customer_id),
	foreign key (approved_by)
	references bago_national_bank.employee (employee_id)
);

insert into bago_national_bank.account (account_type, balance, opened_date, closed_date, status, approved_by, customer_id)
values ('savings', 554.69, '2000-01-10', null, 'active', 1, 2)
;

insert into bago_national_bank.account (account_type, balance, opened_date, closed_date, status, approved_by, customer_id)
values ('checking', 1000, '2001-11-09', null, 'active', 1, 2)
;

insert into bago_national_bank.account (account_type, balance, opened_date, closed_date, status, approved_by, customer_id)
values ('checking', 0, '2009-12-01', '2015-05-07', 'closed', 1, 2)
;


create table bago_national_bank.username (
	username_id			serial			not null,
	username			varchar(20)		not null,
	customer_id			int				    null,
	employee_id			int					null,
	primary key (username_id),
	constraint usernames_fk
	foreign key (customer_id)
	references bago_national_bank.customer (customer_id),
	foreign key (employee_id)
	references bago_national_bank.employee (employee_id)
);

insert into bago_national_bank.username (username, customer_id, employee_id)
values ('bodi23', 2, 1)
;



create table bago_national_bank.pin (
	pin_id				serial			not null,
	pin					varchar(30)		not null,
	username_id			int				not null,
	primary key(pin_id),
	constraint pin_fk
	foreign key (username_id)
	references bago_national_bank.username (username_id)
);

insert into bago_national_bank.pin (pin, username_id)
values ('bd1', 1)
;



create table bago_national_bank.transactions (
	transaction_id			serial			not null,
	transaction_type		varchar(10)		not null,
	amount					float8			not null,
	old_balance				float8			not null,
	new_balance				float8			not null,
	transaction_date		timestamp		default now(),
	account_id				int				not null,
	customer_id				int				not null,
	status					varchar(15)		not null,
	linked_transaction_id	int				not null,
	primary key (transaction_id),
	constraint transactions_fk
	foreign key (account_id)
	references bago_national_bank.account(account_id),
	foreign key(customer_id)
	references bago_national_bank.customer (customer_id)
);




create table bago_national_bank.employee (
	employee_id			serial			not null,
	first_name			varchar(100)	not null,
	last_name			varchar(100)	not null,
	gender				varchar(1)		not null,
	dob					date			not null,
	address1			varchar(50)		not null,
	address2			varchar(50)			null,
	city				varchar(20)		not null,
	state				varchar(4)		not null,
	zip5				varchar(5)		not null,
	zip4				varchar(4)			null,
	phone1				varchar(15)		not null,
	phone2				varchar(15)		not null,
	email				varchar(100)	not null,
	hired_date			date			not null,
	primary key(employee_id)
);	
	


insert into bago_national_bank.employee (first_name, last_name, gender, dob, address1, address2, city, state, zip5, zip4, phone1, phone2, email, hired_date)
values ('Jan', 'Roberts', 'F', '1996-08-20', '3111 W Chandler Blvd', 'Space 1144', 'Chandler', 'AZ', 85226, null, '1384756253', '1957432754', 'abcd@gmail.com', '2015-03-09')
;

