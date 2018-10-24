FROM openjdk:8-jdk-alpine
LABEL maintainer="Andrey Romanov <steampart@gmail.com>"

WORKDIR /usr/src/application

ARG JAR_FILE=build/libs/async-app-db-storage-1.0.jar

ADD ${JAR_FILE} async-app-db-storage-1.0.jar
 
CMD ["java","-jar","async-app-db-storage-1.0.jar"]