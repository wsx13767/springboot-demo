# Admin Server 介面管理
## maven
> mvn clean package
## image
> docker build -t server-admin .
## container
> docker run --name server-admin -e SERVER_PORT=8888 -e EUREKA_SERVER1=127.0.0.1:8761 -e EUREKA_SERVER2=127.0.0.1:8762 -d -p 8888:8888 server-admin
## 介面路徑
> 預設為 localhost:8888
> 使用`spring.boot.admin.client.url`設定admin路徑，
```properties
spring.boot.admin.client.url=http://localhost:8888/admin
```
