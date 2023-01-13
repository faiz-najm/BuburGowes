create table m_product
(
    id            int auto_increment
        primary key,
    product_name  varchar(150) not null,
    product_desc  varchar(200) not null,
    product_price int          not null,
    is_available  int          not null
)
    charset = latin1;

create table m_user
(
    id            int auto_increment
        primary key,
    user_username varchar(200)  not null,
    user_fullname varchar(255)  null,
    user_pass     varchar(100)  not null,
    user_address  varchar(150)  not null,
    user_phone    varchar(20)   not null,
    user_type     int           not null,
    auth_token    int default 0 not null,
    status        int(1)        not null,
    user_saldo    int default 0 not null,
    constraint m_user_user_username_uindex
        unique (user_username)
)
    charset = latin1;

create table m_orders
(
    orderNumber  varchar(50) not null
        primary key,
    tanggalPesan timestamp   null,
    comments     text        null,
    alamat       text        null,
    status       varchar(15) null,
    id_m_user    int         not null,
    constraint m_orders_ibfk_1
        foreign key (id_m_user) references m_user (id)
            on update cascade
)
    charset = latin1;;

create table m_orderdetails
(
    id_m_orders   varchar(50) not null,
    id_m_product  int         not null,
    jumlahPesanan int         null,
    totalHarga    int         null,
    primary key (id_m_orders, id_m_product),
    constraint m_orderdetails_m_product_id_fk
        foreign key (id_m_product) references m_product (id)
            on update cascade on delete cascade,
    constraint orderdetails_orders_id_fk
        foreign key (id_m_orders) references m_orders (orderNumber)
            on update cascade on delete cascade
)
    charset = latin1;;

create index id_m_user
    on m_orders (id_m_user);

INSERT INTO db_pbo_buburgowes.m_user (user_username, user_fullname, user_pass, user_address, user_phone, user_type, auth_token, status, user_saldo) VALUES ('user', 'User', '12345678', '', '09876654321', 2, 0, 1, 0);
INSERT INTO db_pbo_buburgowes.m_user (user_username, user_fullname, user_pass, user_address, user_phone, user_type, auth_token, status, user_saldo) VALUES ('admin', 'Admin', '12345678', '', '09876654321', 1, 7969398, 1, 0);

INSERT INTO db_pbo_buburgowes.m_product (id, product_name, product_desc, product_price, is_available) VALUES (1, 'Bubur Polos', 'Beuh Mantap', 7000, 1);
INSERT INTO db_pbo_buburgowes.m_product (id, product_name, product_desc, product_price, is_available) VALUES (2, 'Bubur Ayam', 'Wenaaak', 12000, 1);
INSERT INTO db_pbo_buburgowes.m_product (id, product_name, product_desc, product_price, is_available) VALUES (3, 'Bubur Ayam Ati Ampela', 'Bergizi Dan Sehat', 15000, 1);


