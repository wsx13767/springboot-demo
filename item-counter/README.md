# 下載後執行

```shell
mvn clean install -Dmaven.test.skip=true
docker-compose up -d
```



# 開啟瀏覽器會到spring admin

http://localhost



# swagger-ui

http://localhost/swagger-ui/index.html



---
# 相關環境設定
------
* API

|   參數    |   預設值   | 描述 |
| ---- | ---- | ---- |
|  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE    |  http://`eureka-server`:8761/eureka  | 註冊eureka server的url |
|  SPRING_DATASOURCE_URL    |  jdbc:mysql://`mysql-db`:3306/mysqldb?serverTimezone=Asia/Taipei&characterEncoding=utf-8  | db連線資訊 |
|  EVOLUTIVELABS_REDIS_HOSTNAME   |  `evo-redis`  | Redis |
| SPRING_DATASOURCE_USERNAME | root | db連線帳號 |
| SPRING_DATASOURCE_PASSWORD | password | db連線密碼 |
| EVOLUTIVELABS_GATEWAY_HOSTNAME | localhost:80 | gateway對外開放的url |
| EVOLUTIVELABS_LOGGING_PATH_FILTER | /api | router log過濾 |
| EVOLUTIVELABS_SWAGGER_PATH | /api | 於swagger ui顯示的路徑 |
| EVOLUTIVELABS_BATCH_SYSTEM | test | 用於執行資料庫資料表batch_schedule排程使用 |

* BATCH

|   參數    |   預設值   | 描述 |
| ---- | ---- | ---- |
|  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE    |  http://`eureka-server`:8761/eureka  | 註冊eureka server的url |
|  SPRING_DATASOURCE_URL    |  jdbc:mysql://`mysql-db`:3306/mysqldb?serverTimezone=Asia/Taipei&characterEncoding=utf-8  | db連線資訊 |
|  EVOLUTIVELABS_REDIS_HOSTNAME   |  `evo-redis`  | Redis |
| SPRING_DATASOURCE_USERNAME | root | db連線帳號 |
| SPRING_DATASOURCE_PASSWORD | password | db連線密碼 |
| EVOLUTIVELABS_GATEWAY_HOSTNAME | localhost:80 | gateway對外開放的url |
| EVOLUTIVELABS_LOGGING_PATH_FILTER | /api | router log過濾 |
| EVOLUTIVELABS_SWAGGER_PATH | /api | 於swagger ui顯示的路徑 |