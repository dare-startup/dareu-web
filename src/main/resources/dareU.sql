create database dareu; 

use dareu; 
create table dareu_user(
	id varchar(32)not null primary key, 
    name varchar(150) not null, 
    email varchar(100)not null, 
    username varchar(100)not null, 
    password varbinary(255)not null, 
    user_since_date datetime not null,
	security_token varchar(32),
    gcm_reg_id varchar(200), 
    coins int not null default 1, 
	role int not null,
	image_path varchar(500),
    uscore int not null default 1,
	birthday varchar(8),
    verified tinyint default 0);
    
create table category(
	id varchar(32) not null primary key, 
    name varchar(100)not null, 
    description varchar(200)); 
    
create table dare(
	id varchar(32) not null primary key, 
    name varchar(100)not null, 
    description varchar(500) not null, 
    category_id varchar(32) not null, 
    estimated_dare_time int not null default 24, 
    coins_prize int, 
    approved tinyint default 0, 
    accepted tinyint default 0,
    creation_date datetime not null, 
	current_security_token varchar(50)not null,
    foreign key(category_id)references category(id));
    
create table dare_package(
	id varchar(32) not null primary key, 
    description varchar(500)not null, 
    price double not null, 
    creation_date datetime not null,
    coins_count int not null); 
    
create table dare_response(
	id varchar(32) not null primary key, 
    response_date datetime not null, 
    video_url varchar(500)not null, 
    views_count int not null default 0); 
    
create table response_like(
	id varchar(32) not null primary key, 
    like_date datetime not null, 
    user_id varchar(32) not null, 
    foreign key(user_id)references dareu_user(id)); 
    
create table response_comment(
	id varchar(32) not null primary key, 
    creation_date datetime not null, 
    comment varchar(200)not null,
    user_id varchar(32) not null, 
    foreign key(user_id)references dareu_user(id)); 
    
create table response_comment_like(
	id varchar(32) not null primary key, 
    creation_date datetime not null, 
    comment_id varchar(32) not null, 
    user_id varchar(32) not null, 
    foreign key(comment_id)references response_comment(id), 
    foreign key(user_id)references dareu_user(id)); 
    
    
create table dareu_user_dare(
	id varchar(32) not null primary key, 
    challenger_id varchar(32) not null, 
    challenged_id varchar(32) not null, 
    dare_id varchar(32) not null,
    foreign key(challenger_id)references dareu_user(id), 
    foreign key(challenged_id)references dareu_user(id), 
    foreign key(dare_id)references dare(id)); 
    
create table dareu_user_package(
	id varchar(32) not null primary key, 
    package_id varchar(32) not null, 
    user_id varchar(32) not null, 
    foreign key(package_id)references dare_package(id), 
    foreign key(user_id)references dareu_user(id)); 
    
create table dare_dare_response(
	id varchar(32) not null primary key , 
    dare_id varchar(32) not null, 
    dare_response_id varchar(32) not null, 
    foreign key(dare_id)references dare(id), 
    foreign key(dare_response_id)references dare_response(id)); 
    
create table dare_response_response_comment(
	id varchar(32) not null primary key , 
    dare_response_id varchar(32) not null, 
    response_comment_id varchar(32) not null, 
    foreign key(dare_response_id)references dare_response(id), 
    foreign key(response_comment_id)references response_comment(id)); 
    
create table dare_response_response_like(
	id varchar(32) not null primary key, 
    dare_response_id varchar(32) not null, 
    response_like_id varchar(32) not null, 
    foreign key(dare_response_id)references dare_response(id), 
    foreign key(response_like_id)references response_like(id)); 
    
create table response_comment_comment_like(
	id varchar(32) not null primary key, 
    response_comment_id varchar(32) not null, 
    comment_like_id varchar(32) not null
    ); 
    