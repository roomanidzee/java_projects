FROM openjdk:8-jdk-alpine
MAINTAINER Andrey Romanov <steampart@gmail.com>

WORKDIR /usr/src/application

ARG JAR_FILE=build/libs/reactivecontracts-mongo-1.0.jar

ADD ${JAR_FILE} reactivecontracts-mongo-1.0.jar
 
CMD ["java", "-Xmx200m", "-jar","reactivecontracts-mongo-1.0.jar"]