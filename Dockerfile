FROM maven:3.9.6-eclipse-temurin-21-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install -DskipTests=true

FROM amazoncorretto:21.0.2-alpine3.19
ARG APPNAME=dev-tool
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/${APPNAME}-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]