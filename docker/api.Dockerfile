FROM openjdk:11-jdk-slim as builder
RUN ["mvn", "install", "-Dmaven.test.skip=true"]

ARG JAR_FILE=iot-api/target/iot-api-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ADD ${JAR_FILE} iot-api.jar


ENTRYPOINT ["java","-jar","/iot-api.jar"]
