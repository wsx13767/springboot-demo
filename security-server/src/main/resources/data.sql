INSERT INTO user (name, id, password, create_time, status) values ('Amy', 'amy123', '123', '2021-11-13 14:23:20', true);
INSERT INTO user (name, id, password, create_time, status) values ('John', 'john123', '123', '2021-11-13 14:23:20', true);
INSERT INTO user (name, id, password, create_time, status) values ('Tony', 'tony123', '123', '2021-11-13 14:23:20', true);
INSERT INTO user (name, id, password, create_time, status) values ('Judy', 'judy123', '123', '2021-11-13 14:23:20', true);
INSERT INTO user (name, id, password, create_time, status) values ('Lucas', 'lucas123', '123', '2021-11-13 14:23:20', true);


INSERT INTO menu (pattern) values ('/testRole/admin/**');
INSERT INTO menu (pattern) values ('/testRole/user/**');
INSERT INTO menu (pattern) values ('/testRole/guest/**');

INSERT INTO menu_role (mid, rid) values (1, 1);
INSERT INTO menu_role (mid, rid) values (2, 1);
INSERT INTO menu_role (mid, rid) values (3, 1);
INSERT INTO menu_role (mid, rid) values (1, 2);
INSERT INTO menu_role (mid, rid) values (2, 2);
INSERT INTO menu_role (mid, rid) values (2, 3);

INSERT INTO role (name, name_zh) values ('ADMIN', '系統管理員');
INSERT INTO role (name, name_zh) values ('USER', '普通用戶');
INSERT INTO role (name, name_zh) values ('GUEST', '遊客');

INSERT INTO user_role (uid, rid) values ('amy123', 3);
INSERT INTO user_role (uid, rid) values ('john123', 2);
INSERT INTO user_role (uid, rid) values ('tony123', 2);
INSERT INTO user_role (uid, rid) values ('judy123', 2);
INSERT INTO user_role (uid, rid) values ('lucas123', 1);
