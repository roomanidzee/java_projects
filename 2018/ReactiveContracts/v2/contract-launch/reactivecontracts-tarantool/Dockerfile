FROM openjdk:8-jdk-alpine
MAINTAINER Andrey Romanov <steampart@gmail.com>

WORKDIR /usr/src/application

ARG JAR_FILE=build/libs/reactivecontracts-tarantool-1.0.jar

ADD ${JAR_FILE} reactivecontracts-tarantool-1.0.jar
 
CMD ["java", "-Xmx200m", "-jar","reactivecontracts-tarantool-1.0.jar", "-Dspring.profiles.active=docker"]