create table gift_certificate
(
    gc_id               bigint  auto_increment
        primary key,
    gc_name             varchar(50)                         not null,
    gc_description      varchar(200)                        not null,
    gc_price            decimal(15, 5)                     not null,
    gc_duration         int                                 not null,
    gc_create_date      timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    gc_last_update_date timestamp(3) default CURRENT_TIMESTAMP(3) on update CURRENT_TIMESTAMP(3) not null
);

create table tag
(
    t_id   bigint  auto_increment
        primary key,
    t_name varchar(50) not null
);

create table `user`
(
    u_id   bigint  auto_increment
        primary key,
    u_name varchar(30) null,
    constraint u_name
        unique (u_name)
);

create table user_order
(
    o_id bigint  auto_increment
        primary key,
    o_price decimal(15, 5) null,
    o_purchase_date timestamp(3) default CURRENT_TIMESTAMP(3) on update CURRENT_TIMESTAMP(3) not null
);

create table tag_m2m_gift_certificate
(
    tmgc_t_id  bigint  not null,
    tmgc_gc_id bigint  not null,
    primary key (tmgc_t_id, tmgc_gc_id),
    constraint fk_tag_m2m_gift_certificate_gift_cert
        foreign key (tmgc_gc_id) references gift_certificate (gc_id)
            on update cascade on delete cascade,
    constraint fk_tag_m2m_gift_certificate_tag
        foreign key (tmgc_t_id) references tag (t_id)
            on update cascade on delete cascade
);

create table order_m2m_certificate
(
    omc_o_id  bigint  not null,
    omc_gc_id bigint  not null,
    primary key (omc_o_id, omc_gc_id),
    constraint fk_order_m2m_certificate_cert
        foreign key (omc_gc_id) references gift_certificate (gc_id)
            on update cascade on delete cascade,
    constraint fk_order_m2m_certificate_order
        foreign key (omc_o_id) references user_order (o_id)
            on update cascade on delete cascade
);

create table user_m2m_order
(
    umo_u_id bigint  not null,
    umo_o_id bigint  not null,
    primary key (umo_u_id, umo_o_id),
    constraint umo_o_id_UNIQUE
        unique (umo_o_id),
    constraint fk_user_m2m_order_order
        foreign key (umo_o_id) references user_order (o_id)
            on update cascade on delete cascade,
    constraint fk_user_m2m_order_user
        foreign key (umo_u_id) references `user` (u_id)
            on update cascade on delete cascade
);




INSERT INTO gift_certificate (gc_id, gc_name, gc_description, gc_price, gc_duration, gc_create_date, gc_last_update_date)
VALUES (1,'Cert1','Cert1A',100.5,30,'2022-01-10 10:10:10','2022-01-10 10:10:12');

INSERT INTO gift_certificate (gc_id, gc_name, gc_description, gc_price, gc_duration, gc_create_date, gc_last_update_date)
VALUES (2,'Cert2','Cert2A',100.5,30,'2022-01-10 10:10:12','2022-01-10 10:10:14');

INSERT INTO gift_certificate (gc_id, gc_name, gc_description, gc_price, gc_duration, gc_create_date, gc_last_update_date)
VALUES (3,'Cert3','Cert3A',100.5,30,'2022-01-10 10:10:14','2022-01-10 10:10:16');

INSERT INTO user_order (o_id,o_price)
VALUES (1,100);

INSERT INTO order_m2m_certificate (omc_o_id,omc_gc_id)
VALUES(1,1)

