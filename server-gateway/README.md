# GATEWAY 架設
## maven
> mvn clean package
## docker
### image
> docker build -t server-gateway:1.0.0 .
### container
> docker run --name server-gateway -d --env-file env.list -p 8080:8080 server-gateway:1.0.0

---------
env.list 內容
```
SERVER_PORT=8080
EUREKA_SERVER_HOST_ONE=
EUREKA_SERVER_HOST_TWO=
EUREKA_INSTANCE_IP_ADDRESS=
```
