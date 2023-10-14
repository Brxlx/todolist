FROM ubuntu:22.04 AS BUILD

RUN apt-get update && apt-get install openjdk-17-jdk -y

COPY . .

RUN apt-get install maven -y

RUN mvn clean install


# 
FROM openjdk:17-jdk-slim
COPY --from=BUILD /target/todolist-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT [ "java", "-jar", "app.jar" ]
