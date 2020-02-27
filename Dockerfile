FROM maven:3.6.3-jdk-11-slim as build
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
VOLUME /app
COPY --from=build /usr/src/app/target/urlshrinker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]