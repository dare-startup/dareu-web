create database dareu; 

use dareu; 
create table dareu_user(
	id varchar(36)not null primary key, 
    name varchar(150) not null, 
    email varchar(100)not null, 
    username varchar(100)not null, 
    password varbinary(255)not null, 
    user_since_date varchar(10) not null,
	security_token varchar(40),
    gcm_reg_id varchar(200), 
    coins int not null default 1, 
	role int not null,
	image_path varchar(500),
    uscore int not null default 1,
	birthday varchar(10),
    verified tinyint default 0);
    
create table category(
	id varchar(36) not null primary key, 
    name varchar(100)not null, 
    description varchar(200)); 
    
create table dare(
	id varchar(36) not null primary key, 
    name varchar(100)not null, 
    description varchar(500) not null, 
    category_id varchar(36) not null, 
    estimated_dare_time int not null default 24,  
    approved tinyint default 0, 
    accepted tinyint default 0,
    accepted_date varchar(30),
    creation_date datetime not null, 
    foreign key(category_id)references category(id));
    
create table package(
	id varchar(36) not null primary key, 
    description varchar(500)not null, 
    price double not null, 
    creation_date datetime not null,
    coins_count int not null); 
    
create table dare_response(
	id varchar(36) not null primary key, 
    response_date datetime not null, 
    video_url varchar(500)not null, 
    views_count int not null default 0, 
	likes int not null default 0); 
   
    
create table response_comment(
	id varchar(36) not null primary key, 
    creation_date datetime not null, 
    comment varchar(200)not null,
    user_id varchar(36) not null, 
    foreign key(user_id)references dareu_user(id)); 
   
create table friendship(
	id varchar(36)not null primary key, 
	user_id varchar(36)not null, 
	requested_user_id varchar(36)not null, 
	request_date varchar(8)not null, 
	accepted int default 0, 
	foreign key(user_id)references dareu_user(id), 
	foreign key(requested_user_id)references dareu_user(id)
);
    
create table dareu_user_dare(
	id varchar(36) not null primary key, 
    challenger_id varchar(36) not null, 
    challenged_id varchar(36) not null, 
    dare_id varchar(36) not null,
    foreign key(challenger_id)references dareu_user(id), 
    foreign key(challenged_id)references dareu_user(id), 
    foreign key(dare_id)references dare(id)); 
    
create table dareu_user_package(
	id varchar(36) not null primary key, 
    package_id varchar(36) not null, 
    user_id varchar(36) not null, 
    foreign key(package_id)references dare_package(id), 
    foreign key(user_id)references dareu_user(id)); 
    
create table dare_dare_response(
	id varchar(36) not null primary key , 
    dare_id varchar(36) not null, 
	user_id varchar(36)not null,
    dare_response_id varchar(36) not null, 
    foreign key(dare_id)references dare(id), 
    foreign key(dare_response_id)references dare_response(id), 
	foreign key(user_id)references dareu_user(id)); 
    
create table dare_response_response_comment(
	id varchar(36) not null primary key , 
    dare_response_id varchar(36) not null, 
    response_comment_id varchar(36) not null, 
    foreign key(dare_response_id)references dare_response(id), 
    foreign key(response_comment_id)references response_comment(id)); 
    
CREATE or REPLACE VIEW v_friendship AS
	select f.user_id, f.friend_name, f.friend_id, IFNULL(dcount.count,0) 'dare_count' , IFNULL(vcount.count, 0) 'video_count'
	from (
	select distinct du.id 'friend_id', du.name 'friend_name', f.user_id  from 
		friendship f inner join dareu_user du on du.id = f.requested_user_id
		where f.accepted = 1
		union
		select distinct du.id 'friend_id', du.name 'friend_name', f.requested_user_id 'user_id' from 
		friendship f inner join dareu_user du on du.id = f.user_id
		where f.accepted = 1
	) f 
	left join (
	select count(*) 'count', challenger_id from dareu_user_dare group by challenger_id) dcount
	on f.friend_id = dcount.challenger_id
	left join (
	select user_id, count(*) 'count' from dare_dare_response group by user_id
	) vcount
	on vcount.user_id = f.friend_id;