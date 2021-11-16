insert into route_group (id, status, description) values ('api', true, 'api群組');
insert into route_group (id, status, description) values ('admin', false, '設定群組');

insert into api_server (id, group_id, name, uri, status, description)
    values ('provider', 'api', 'provider', 'lb://SERVER-PROVIDER', true, '測試用的api server');

insert into api_route (server_id, path, before, after, status, description)
    values ('provider', '/info', null, '2021-11-12 14:10:00', true, '取得相關資訊');
insert into api_route (server_id, path, before, after, status, description)
        values ('provider', '/hello', '2021-11-12 14:10:00', null, true, '測試用');