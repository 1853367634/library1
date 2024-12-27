-- 删除已存在的数据库和表
DROP DATABASE IF EXISTS `library`;
CREATE DATABASE `library` DEFAULT CHARACTER SET utf8mb4;

USE `library`;

-- 创建用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表';

-- 创建图书表
CREATE TABLE `book` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID',
    `name` VARCHAR(100) NOT NULL COMMENT '书名',
    `author` VARCHAR(50) NOT NULL COMMENT '作者',
    `publisher` VARCHAR(100) NOT NULL COMMENT '出版社',
    `category` VARCHAR(50) NOT NULL COMMENT '分类',
    `description` TEXT COMMENT '图书简介',
    `stock` INT NOT NULL DEFAULT 1 COMMENT '库存数量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '图书表';

-- 创建借阅记录表
CREATE TABLE `borrow_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `book_id` BIGINT NOT NULL COMMENT '图书ID',
    `borrow_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    `return_time` DATETIME COMMENT '归还时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-借阅中，1-已归还',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
) COMMENT '借阅记录表';

-- 插入初始用户数据
INSERT INTO `user` (username, password, name, phone, role) VALUES
('admin', '123456', '超级管理员', '13800000001', 'SUPER_ADMIN'),
('librarian', '123456', '图书管理员', '13800000002', 'ADMIN'),
('zhangsan', '123456', '张三', '13800000003', 'USER'),
('lisi', '123456', '李四', '13800000004', 'USER'),
('wangwu', '123456', '王五', '13800000005', 'USER');

-- 插入初始图书数据
INSERT INTO `book` (name, author, publisher, category, description, stock) VALUES
('Java编程思想（第4版）', 'Bruce Eckel', '机械工业出版社', '计算机技术', 'Java领域的经典著作，全面介绍了Java编程的核心概念和技术。', 5),
('算法导论（第3版）', 'Thomas H.Cormen', '机械工业出版社', '计算机技术', '系统地介绍了计算机算法的设计与分析。', 3),
('深入理解计算机系统（第3版）', 'Randal E.Bryant', '机械工业出版社', '计算机技术', '计算机系统底层原理的经典教材。', 4),
('百年孤独', '加西亚·马尔克斯', '南海出版公司', '文学小说', '魔幻现实主义文学的代表作。', 2),
('三体（全集）', '刘慈欣', '重庆出版社', '科幻小说', '中国科幻文学的里程碑之作。', 3),
('人类简史', '尤瓦尔·赫拉利', '中信出版社', '历史', '从生物学角度回顾人类历史的演化。', 4),
('经济学原理（第7版）', '曼昆', '北京大学出版社', '经济学', '经济学入门的经典教材。', 3),
('活着', '余华', '作家出版社', '文学小说', '讲述了一个人一生的故事，展现了生命的意义。', 5),
('红楼梦', '曹雪芹', '人民文学出版社', '古典文学', '中国古典四大名著之一，描写了贾府的兴衰。', 3),
('时间简史', '史蒂芬·霍金', '湖南科学技术出版社', '科普读物', '探讨宇宙起源与命运的科普经典。', 4),
('JavaScript高级程序设计（第4版）', 'Matt Frisbie', '人民邮电出版社', '计算机技术', 'JavaScript语言的权威指南。', 3),
('Python编程：从入门到实践', 'Eric Matthes', '人民邮电出版社', '计算机技术', 'Python编程的入门指南。', 5),
('围城', '钱钟书', '人民文学出版社', '文学小说', '描写了中国知识分子的生活状态。', 4),
('史记（文白对照本）', '司马迁', '岳麓书社', '历史', '中国第一部纪传体通史。', 2),
('自控力', 'Kelly McGonigal', '文化发展出版社', '心理学', '探讨意志力的科学，提供实用的自我管理建议。', 3),
('Spring实战（第6版）', 'Craig Walls', '人民邮电出版社', '计算机技术', 'Spring框架实战指南，包含Spring Boot和Spring Cloud。', 4),
('西游记', '吴承恩', '人民文学出版社', '古典文学', '中国古典四大名著之一，讲述了师徒四人西天取经的故事。', 3),
('水浒传', '施耐庵', '人民文学出版社', '古典文学', '中国古典四大名著之一，描写了108位好汉的故事。', 3),
('三国演义', '罗贯中', '人民文学出版社', '古典文学', '中国古典四大名著之一，描写了三国时期的历史故事。', 3),
('平凡的世界', '路遥', '北京十月文艺出版社', '文学小说', '描写了中国农村生活的长篇小说。', 4);

-- 添加外键约束
ALTER TABLE `borrow_record` ADD CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
ALTER TABLE `borrow_record` ADD CONSTRAINT `fk_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`); 