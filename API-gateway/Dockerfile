FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/API-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]