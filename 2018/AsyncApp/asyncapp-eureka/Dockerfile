FROM openjdk:8-jdk-alpine
LABEL maintainer="Andrey Romanov <steampart@gmail.com>"

WORKDIR /usr/src/application

EXPOSE 9000

ARG JAR_FILE=build/libs/async-app-eureka-1.0.jar

ADD ${JAR_FILE} async-app-eureka-1.0.jar
 
CMD ["java","-jar","async-app-eureka-1.0.jar"]