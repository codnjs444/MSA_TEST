# ① 빌드 단계
FROM gradle:8.6.0-jdk21 AS build
COPY --chown=gradle:gradle . /workspace
WORKDIR /workspace
# 실행 JAR 빌드
RUN gradle clean bootJar -x test

# ② 실행 단계
FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/workspace/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
# Config Server 기본 포트
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar"]
