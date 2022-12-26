
    create table lofi (
       lofi_id bigint not null auto_increment,
        lofi_author varchar(255),
        lofi_location varchar(255) not null,
        lofi_name varchar(255) not null,
        lofi_playtime varchar(255),
        lofi_type varchar(255) not null,
        primary key (lofi_id)
    ) engine=InnoDB;

    create table lofi_pool (
       lofi_pool_id bigint not null auto_increment,
        lofi_pool_genre varchar(255) not null,
        lofi_pool_name varchar(255) not null,
        primary key (lofi_pool_id)
    ) engine=InnoDB;

    create table lofi_pool_assignment (
       lofi_pool_id bigint not null,
        lofi_id bigint not null
    ) engine=InnoDB;

    create table playlist (
       playlist_id bigint not null auto_increment,
        playlist_created datetime(6) not null,
        playlist_genre varchar(255),
        playlist_name varchar(255) not null,
        playlist_status varchar(255) not null,
        playlist_updated datetime(6) not null,
        playlist_version integer not null,
        user_user_id bigint,
        primary key (playlist_id)
    ) engine=InnoDB;

    create table playlist_lofi_assignment (
       lofi_id bigint not null,
        playlist_id bigint not null,
        playlist_lofi_assignment_time datetime(6) not null,
        primary key (lofi_id, playlist_id)
    ) engine=InnoDB;

    create table user (
       user_id bigint not null auto_increment,
        user_created datetime(6),
        user_name varchar(255) not null,
        user_password varchar(255) not null,
        user_type varchar(255) not null,
        user_updated datetime(6),
        user_version integer not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table lofi 
       add constraint UK_r15rpgsiss9uqjekfm08xaxee unique (lofi_location);

    alter table lofi 
       add constraint UK_6e31qmbqve00i0ygdeh60q0rx unique (lofi_name);

    alter table lofi_pool 
       add constraint UK_1c5q67qj8qsl4s53o62ykkunq unique (lofi_pool_name);

    alter table user 
       add constraint UK_lqjrcobrh9jc8wpcar64q1bfh unique (user_name);

    alter table lofi_pool_assignment 
       add constraint FKf1yieiam6wwhghur9189quihe 
       foreign key (lofi_id) 
       references lofi (lofi_id);

    alter table lofi_pool_assignment 
       add constraint FK754l2wphll0fujfoyi3caqygi 
       foreign key (lofi_pool_id) 
       references lofi_pool (lofi_pool_id);

    alter table playlist 
       add constraint FKddxtaugjepdgf6gj8hvk8mvmk 
       foreign key (user_user_id) 
       references user (user_id);

    alter table playlist_lofi_assignment 
       add constraint FK9h7oacgvk6kfdtyltgcapk3iw 
       foreign key (lofi_id) 
       references lofi (lofi_id);

    alter table playlist_lofi_assignment 
       add constraint FK8yc9na81sg2rvft1f24go5l1m 
       foreign key (playlist_id) 
       references playlist (playlist_id);

    create table playlist_lofi_assignment (
       lofi_id bigint not null,
        playlist_id bigint not null,
        playlist_lofi_assignment_time datetime(6) not null,
        primary key (lofi_id, playlist_id)
    ) engine=InnoDB;

    alter table playlist_lofi_assignment 
       add constraint FK9h7oacgvk6kfdtyltgcapk3iw 
       foreign key (lofi_id) 
       references lofi (lofi_id);

    alter table playlist_lofi_assignment 
       add constraint FK8yc9na81sg2rvft1f24go5l1m 
       foreign key (playlist_id) 
       references playlist (playlist_id);
