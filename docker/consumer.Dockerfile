FROM openjdk:11-jdk-slim as builder

ARG JAR_FILE=iot-consumer/target/iot-consumer-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ADD ${JAR_FILE} iot-consumer.jar

ENTRYPOINT ["java","-jar","/iot-consumer.jar"]