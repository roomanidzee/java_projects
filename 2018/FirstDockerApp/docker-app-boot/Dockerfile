FROM openjdk:8-jdk-alpine

WORKDIR /usr/src/application

EXPOSE 8080

COPY . .

ENTRYPOINT ["./gradlew", "bootRun"]