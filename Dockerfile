#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests -Dspring-boot.run.jvmArguments="-Dspring.run.profiles=dev"

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/doggo_backend-1.jar /usr/local/lib/doggo_backend-1.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/local/lib/doggo_backend-1.jar", "-Dspring.profiles.active=dev"]