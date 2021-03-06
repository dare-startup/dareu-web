create database dareu;
use dareu;
create table dareu_user(
	id varchar(36)not null primary key, 
    name varchar(150) not null, 
    email varchar(100)not null, 
    password varbinary(255)not null, 
    user_since_date varchar(10) not null,
    security_token varchar(40),
    image_url varchar(300),
    gcm_reg_id varchar(200), 
    coins int not null default 1, 
    role int not null,
    uscore int not null default 1,
    birthday varchar(10),
    verified tinyint default 0,
    g_id varchar(100),
    account_type int);

create table contact_message(
    id varchar(36) not null primary key, 
    name varchar(50)not null, 
    email varchar(100),
    comment varchar(300),
    message_date varchar(50)not null, 
    status int not null
);

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
    response_date varchar(50) not null, 
    views_count int not null default 0, 
    dare_id varchar(36)not null, 
    user_id varchar(36) not null, 
    last_update varchar(50) not null,
    thumb_url varchar(300)not null, 
    video_url varchar(300) not null,
    foreign key(user_id) references dareu_user(id),
    foreign key(dare_id)references dare(id)); 


create table response_comment(
    id varchar(36) not null primary key, 
    comment_date varchar(50) not null, 
    comment varchar(100)not null,
    response_id varchar(36) not null, 
    user_id varchar(36)not null, 
    foreign key(response_id) references dare_response(id), 
    foreign key(user_id)references dareu_user(id)
);

create table comment_clap(
    id varchar(36)not null primary key,
    comment_id varchar(36)not null,
    user_id varchar(36)not null,
    clap_date varchar(50)not null,
    foreign key(comment_id)references response_comment(id),
    foreign key(user_id) references dareu_user(id)
);

create table response_clap(
    id varchar(36)not null primary key,
    clap_date varchar(50)not null,
    response_id varchar(36)not null, 
    user_id varchar(36) not null, 
    foreign key(response_id) references dare_response(id), 
    foreign key(user_id)references dareu_user(id)
);

create table friendship(
	id varchar(36)not null primary key, 
	user_id varchar(36)not null, 
	requested_user_id varchar(36)not null, 
	request_date varchar(8)not null, 
	accepted int default 0, 
	foreign key(user_id)references dareu_user(id), 
	foreign key(requested_user_id)references dareu_user(id)
);

create table response_anchor(
    id varchar(36)not null primary key, 
    user_id varchar(36)not null, 
    response_id varchar(36)not null, 
    creation_date varchar(50)not null, 
foreign key(user_id) references dareu_user(id),
foreign key(response_id)references dare_response(id)
);