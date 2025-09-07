FROM eclipse-temurin:21-jre-alpine

COPY build/libs/cs-events-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]