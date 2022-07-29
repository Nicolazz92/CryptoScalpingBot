FROM openjdk:14-alpine
COPY ./target/database-provider-1.0-SNAPSHOT.jar /tmp/app.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "app.jar"]