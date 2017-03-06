create database dareu; 

use dareu; 

-- DAREU USER TABLE
create table dareu_user(
	id varchar(36)not null primary key, 
    name varchar(150) not null, 
    email varchar(100)not null, 
    password varbinary(255)not null, 
    user_since_date varchar(10) not null,
    security_token varchar(40),
    gcm_reg_id varchar(200), 
    coins int not null default 1, 
    role int not null,
    uscore int not null default 1,
    birthday varchar(10),
    verified tinyint default 0);

-- CONTACT MESSAGE TABLE
create table contact_message(
    id varchar(36) not null primary key, 
    name varchar(50)not null, 
    email varchar(100),
    comment varchar(300),
    message_date varchar(50)not null, 
    status int not null
);
    
-- CATEGORY TABLE
create table category(
    id varchar(36) not null primary key, 
    name varchar(100)not null, 
    description varchar(200));

-- DARE FLAG TABLE
create table dare_flag(
    id varchar(36)not null primary key, 
    comment varchar(100)not null, 
    flag_date varchar(50)not null
); 
    
--DARE TABLE
create table dare(
    id varchar(36) not null primary key, 
    name varchar(100)not null, 
    description varchar(500) not null, 
    category_id varchar(36) not null, 
    estimated_dare_time int not null default 24,
    approved tinyint default 0, 
    answered tinyint default 0,
    accepted tinyint default 0,
    accepted_date varchar(30),
    creation_date datetime not null, 
    challenged_user_id varchar(36)not null, 
    challenger_user_id varchar(36) not null, 
    completed tinyint default 0,
    declined tinyint default 0, 
    expired tinyint default 0,
    flag varchar(36), 
    foreign key(category_id)references category(id),
    foreign key(id) references dare_flag(id),
    foreign key(challenger_user_id) references dareu_user(id), 
    foreign key(challenged_user_id)references dareu_user(id)); 


    
-- DARE RESPONSE (VIDEO)
create table dare_response(
    id varchar(36) not null primary key, 
    comment varchar(100)not null,
    response_date varchar(50) not null, 
    views_count int not null default 0, 
    likes int not null default 0, 
    dare_id varchar(36)not null, 
    user_id varchar(36) not null, 
    last_update varchar(50) not null,
    foreign key(user_id) references dareu_user(id),
    foreign key(dare_id)references dare(id)); 
   
-- FRIENDSHIP TABLE
create table friendship(
	id varchar(36)not null primary key, 
	user_id varchar(36)not null, 
	requested_user_id varchar(36)not null, 
	request_date varchar(8)not null, 
	accepted int default 0, 
	foreign key(user_id)references dareu_user(id), 
	foreign key(requested_user_id)references dareu_user(id)
);
    
-- DARE REQUEST TABLE
create table dare_request(
    id varchar(36) not null primary key, 
    challenger_id varchar(36) not null, 
    challenged_id varchar(36) not null, 
    dare_id varchar(36) not null,
    foreign key(challenger_id)references dareu_user(id), 
    foreign key(challenged_id)references dareu_user(id), 
    foreign key(dare_id)references dare(id));     


-- DARE RESPONSE COMMENT TABLE 
