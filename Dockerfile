#
# Build stage
#
#FROM maven:3.6.0-jdk-11-slim AS build
FROM maven:3.9.5-amazoncorretto-17
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
#FROM openjdk:11-jre-slim
FROM amazoncorretto:17.0.12-al2
COPY --from=build /home/app/target/doggo_backend-1.jar /usr/local/lib/doggo_backend-1.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/local/lib/doggo_backend-1.jar"]