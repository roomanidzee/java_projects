FROM openjdk:8-jdk-alpine
MAINTAINER Andrey Romanov <steampart@gmail.com>

WORKDIR /usr/src/application

ARG JAR_FILE=build/libs/reactivecontracts-rabbit-1.0.jar

ADD ${JAR_FILE} reactivecontracts-rabbit-1.0.jar
 
CMD ["java", "-Xmx200m", "-jar","reactivecontracts-rabbit-1.0.jar"]