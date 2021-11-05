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
EUREKA_SERVER_HOST_ONE=127.0.0.1:8761 #第一台的eureka server host:port
EUREKA_SERVER_HOST_TWO=127.0.0.1:8762 #第二台的eureka server host:port
EUREKA_INSTANCE_IP_ADDRESS=127.0.0.1 #於eureka指定IP 
```
