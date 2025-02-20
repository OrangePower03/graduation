drop database if exists `graduation`;
create database if not exists `graduation`;
use graduation;

drop table if exists `sys_user`;
create table `sys_user`(
    `id` bigint not null primary key auto_increment comment '主键',
    `username` varchar(64) not null unique comment '用户名',
    `password` varchar(64) not null comment '密码',
    `name` varchar(64) not null comment '用户的真实姓名',
    `id_number` varchar(18) not null unique comment '用户的身份证号',
    `phone` varchar(11) null comment '用户的手机',
    `age` int not null comment '用户的年龄',
    `sex` int not null comment '用户的性别，0男，1女',
    `role_id` bigint not null comment '角色id外键',
    `status` int not null default 0 comment '用户状态，0正常，1停用，2监控',
    `create_time` datetime not null default now() comment '创建时间',
    `update_time` datetime not null default now() comment '更新时间',
    `del_flag` tinyint not null default 0 comment '删除标志，1标识删除'
);

drop table if exists `sys_role`;
create table `sys_role`(
    `id` bigint not null primary key auto_increment comment '主键',
    `name` varchar(64) not null comment '角色名称',
    `permission_key` varchar(64) not null comment '权限名',
    `create_time` datetime not null default now() comment '创建时间',
    `update_time` datetime not null default now() comment '更新时间',
    `del_flag` tinyint not null default 0 comment '删除标志，1标识删除'
);
insert into `sys_role`(id, name, permission_key) values
    (1, '管理员', '__admin__'),
    (2, '年轻人', '__youngster__'),
    (3, '老年人', '__elder__')
;

drop table if exists `people_relation`;
create table `people_relation`(
    `youngster_id` bigint not null comment '年轻人外键',
    `elder_id` bigint not null comment '老年人外键',
    `status` tinyint not null default 1 comment '关系状态，0正常，1未通过',
    primary key (youngster_id, elder_id)
);

/* ----------------------- 指标 ----------------------- */
drop table if exists `indicator`;
create table `indicator`(
    `id` bigint not null primary key auto_increment comment '主键',
    `name` varchar(64) not null comment '指标名称',
    `unit` varchar(64) not null comment '指标的单位',
    `standard_range` varchar(64) not null comment '指标的标准范围',
    `create_time` datetime not null default now() comment '创建时间',
    `update_time` datetime not null default now() comment '更新时间',
    `del_flag` tinyint not null default 0 comment '删除标志，1标识删除'
);
insert into `indicator`(name, unit, standard_range) values
    ('红细胞数（RBC）', '× 10^12/L', '4.5 ~ 5.9;3.9 ~ 5.2'),
    ('血红蛋白（Hb）', 'g/L', '130 ~ 175;115 ~ 150'),
    ('红细胞压积（HCT）', '%', '40 ~ 50;37 ~ 47'),
    ('白细胞数（WBC）', '× 10^9/L', '4.0 ~ 10.0'),
    ('血小板数（PLT）', '× 10^9/L', '100 ~ 300'),
    ('中性粒细胞比率（NE%）', '%', '50 ~ 70'),
    ('淋巴细胞比率（LY%）', '%', '20 ~ 40'),
    ('空腹血糖（FPG）', 'mmol/L', '3.9 ~ 6.1'),
    ('餐后2小时血糖（OGTT）', 'mmol/L', '< 7.8'),
    ('糖化血红蛋白（HbA1c）', '%', '< 5.7'),
    ('总胆固醇（TC）', 'mmol/L', '< 5.2'),
    ('低密度脂蛋白胆固醇（LDL-C）', 'mmol/L', '< 3.4'),
    ('高密度脂蛋白胆固醇（HDL-C）', 'mmol/L', '> 1.0;> 1.3'),
    ('甘油三酯（TG）', 'mmol/L', '< 1.7'),
    ('丙氨酸氨基转移酶（ALT）', 'U/L', '< 40;< 35'),
    ('天冬氨酸氨基转移酶（AST）', 'U/L', '< 40;< 35'),
    ('总蛋白（TP）', 'g/L', '60 ~ 80'),
    ('白蛋白（ALB）', 'g/L', '35 ~ 55'),
    ('总胆红素（TBIL）', 'µmol/L', '5 ~ 20'),
    ('直接胆红素（DBIL）', 'µmol/L', '0 ~ 6'),
    ('血清肌酐（Cr）', 'µmol/L', '60 ~ 110;50 ~ 90'),
    ('尿素氮（BUN）', 'mmol/L', '2.9 ~ 8.2'),
    ('尿酸（UA）', 'mmol/L', '201 ~ 420;150 ~ 375')
;

drop table if exists `elder_indicator`;
create table `elder_indicator`(
    `id` bigint not null primary key auto_increment comment '主键',
    `elder_id` bigint not null comment '老人id',
    `indicator_id` bigint not null comment '指标id',
    `value` decimal not null comment '指标值',
    `normal` int not null comment '指标是否正常，正常为0，异常为1',
    `check_time` date not null comment '体检时间',
    `create_time` datetime not null default now() comment '创建时间',
    `update_time` datetime not null default now() comment '更新时间',
    `del_flag` tinyint not null default 0 comment '删除标志，1标识删除'
);
