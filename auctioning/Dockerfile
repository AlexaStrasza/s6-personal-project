FROM openjdk:15-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw
RUN ./mvnw install -DskipTests

FROM openjdk:15-jdk-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:15-jdk-alpine
#VOLUME /tmp
#COPY /target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]