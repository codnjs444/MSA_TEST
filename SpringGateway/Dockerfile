# ① 빌드 단계
FROM gradle:8.6.0-jdk21 AS build
COPY --chown=gradle:gradle . /workspace
WORKDIR /workspace
# 실행 JAR 빌드
RUN gradle clean bootJar -x test

# ② 런타임 단계
FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/workspace/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
# Gateway 기본 포트
EXPOSE 8080
# Config Server / Eureka 주소
ENV SPRING_CONFIG_IMPORT=optional:configserver:http://config-server:8888/ \
    EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://uereka-server:8761/eureka


ENTRYPOINT ["java","-jar","/app.jar"]
