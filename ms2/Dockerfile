# Build
FROM gradle:8.6.0-jdk21 AS build
COPY --chown=gradle:gradle . /workspace
WORKDIR /workspace
RUN gradle clean bootJar -x test

# Runtime
FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/workspace/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
EXPOSE 8082
ENV EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://uereka-server:8761/eureka
ENTRYPOINT ["java","-jar","/app.jar"]
