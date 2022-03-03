create table gift_certificate
(
    gc_id               bigint unsigned auto_increment
        primary key,
    gc_name             varchar(50)                         not null,
    gc_description      varchar(200)                        not null,
    gc_price            decimal(15, 5)                     not null,
    gc_duration         int                                 not null,
    gc_create_date      timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    gc_last_update_date timestamp(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3)
);

create table tag
(
    t_id   bigint unsigned auto_increment
        primary key,
    t_name varchar(50) not null
);

create table tag_m2m_gift_certificate
(
    tmgc_t_id  bigint unsigned not null,
    tmgc_gc_id bigint unsigned not null,
    primary key (tmgc_t_id, tmgc_gc_id),
    constraint fk_tag_m2m_gift_certificate_gift_cert
        foreign key (tmgc_gc_id) references gift_certificate (gc_id)
            on update cascade on delete cascade,
    constraint fk_tag_m2m_gift_certificate_tag
        foreign key (tmgc_t_id) references tag (t_id)
            on update cascade on delete cascade
);

create table 

INSERT INTO gift_certificate (gc_id, gc_name, gc_description, gc_price, gc_duration, gc_create_date, gc_last_update_date)
VALUES ((1,'Cert1','Cert1A',100.5,30,'2022-01-10 10:10:10.1','2022-01-10 10:10:12.1'),
        (2,'Cert2','Cert2A',100.5,30,'2022-01-10 10:10:12.1','2022-01-10 10:10:14.1'),
        (3,'Cert3','Cert3A',100.5,30,'2022-01-10 10:10:14.1','2022-01-10 10:10:16.1'))
