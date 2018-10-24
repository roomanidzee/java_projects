FROM openjdk:8-jdk-alpine
LABEL maintainer="Andrey Romanov <steampart@gmail.com>"

WORKDIR /usr/src/application

ARG JAR_FILE=build/libs/async-app-rabbit-main-1.0.jar

ADD ${JAR_FILE} async-app-rabbit-main-1.0.jar
 
CMD ["java","-jar","async-app-rabbit-main-1.0.jar"]