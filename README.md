## Run mysql
```
docker network create mixit
docker run --name mysql-mixit --network=mixit -p3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:latest
docker run -it --network mixit --rm mysql mysql -P3306 -hmysql-mixit -uroot -p
CREATE DATABASE mixit
```

## Run the app
```
mvn clean install
java -Dspring.profiles.active=local -jar target/mixit-0.0.1-SNAPSHOT.jar
```