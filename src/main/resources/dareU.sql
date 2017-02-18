create database dareu; 

use dareu; 
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
    
create table category(
    id varchar(36) not null primary key, 
    name varchar(100)not null, 
    description varchar(200));

create table dare_flag(
    id varchar(36)not null primary key, 
    comment varchar(100)not null, 
    flag_date varchar(50)not null
); 
    
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


    
create table dare_response(
    id varchar(36) not null primary key, 
    comment varchar(100)not null,
    response_date datetime not null, 
    views_count int not null default 0, 
    likes int not null default 0, 
    dare_id varchar(36)not null, 
    user_id varchar(36) not null, 
    foreign key(user_id) references dareu_user(id),
    foreign key(dare_id)references dare(id)); 
   
create table friendship(
	id varchar(36)not null primary key, 
	user_id varchar(36)not null, 
	requested_user_id varchar(36)not null, 
	request_date varchar(8)not null, 
	accepted int default 0, 
	foreign key(user_id)references dareu_user(id), 
	foreign key(requested_user_id)references dareu_user(id)
);
    
create table dare_request(
    id varchar(36) not null primary key, 
    challenger_id varchar(36) not null, 
    challenged_id varchar(36) not null, 
    dare_id varchar(36) not null,
    foreign key(challenger_id)references dareu_user(id), 
    foreign key(challenged_id)references dareu_user(id), 
    foreign key(dare_id)references dare(id));     

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