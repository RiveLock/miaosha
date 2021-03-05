create table goods
(
    id int auto_increment,
    name varchar(300) null comment '商品名称',
    price decimal(10,2) comment '价格',
    stock_count int null comment '库存数量',
    sale_count int null comment '销售数量',
    create_time datetime null,
    update_time datetime null,
    constraint goods_pk
        primary key (id)
)
    comment '商品表';

create table goods_order
(
    id int auto_increment,
    goods_id int null comment '商品id',
    create_time datetime null,
    update_time datetime null,
    constraint order_pk
        primary key (id)
)
    comment '商品订单表';

