FROM openjdk:8-jdk-alpine
LABEL maintainer="Andrey Romanov <steampart@gmail.com>"

WORKDIR /usr/src/application

EXPOSE 8082

ARG JAR_FILE=build/libs/async-app-zuul-1.0.jar

ADD ${JAR_FILE} async-app-zuul-1.0.jar
 
CMD ["java","-jar","async-app-zuul-1.0.jar"]