FROM maven:3.8.5-jdk-11 as builder
ARG application-properties
WORKDIR /data
COPY . .
RUN mvn clean package -Dspring.config.location=application.properties

FROM openjdk:11-slim
EXPOSE 8080
COPY --from=builder /data/target /data
ENTRYPOINT ["java", "-jar", "/data/backend-0.0.1-SNAPSHOT.jar"]