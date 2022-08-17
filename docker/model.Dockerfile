FROM openjdk:11-jdk-slim as builder


ARG JAR_FILE=iot-model/target/iot-model-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ADD ${JAR_FILE} iot-model.jar


ENTRYPOINT ["java","-jar","/iot-model.jar"]
