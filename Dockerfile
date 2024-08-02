#
# Build stage
#
#
# Build stage
#
FROM maven:3.9.5-amazoncorretto-17-al2023 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"

#
# Package stage
#
FROM amazoncorretto:17.0.12-al2
COPY --from=build /home/app/target/doggo_backend-1.jar /usr/local/lib/doggo_backend-1.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/local/lib/doggo_backend-1.jar", "-Dspring.profiles.active=dev"]