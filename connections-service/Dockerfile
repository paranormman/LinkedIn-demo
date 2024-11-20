#Use an official OpenJDK runtime as a parent image
FROM maven:3.9.4-eclipse-temurin-21-alpine

#Set the working directory in the container
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline

COPY src ./src
CMD ["./mvnw", "spring-boot:run"]