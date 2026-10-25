FROM eclipse-temurin:25-jre-alpine
ARG APP
COPY apps/${APP}/build/libs/app.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
