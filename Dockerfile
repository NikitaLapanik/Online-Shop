FROM maven:3.9.4-eclipse-temurin-20 as build

COPY src src
COPY pom.xml pom.xml

RUN mvn clean package dependency:copy-dependencies -DskipTests

FROM eclipse-temurin:20

VOLUME /tmp

COPY --from=build target/*.jar app.jar
COPY --from=build target/dependency/* ./lib

ENTRYPOINT ["java", "-cp", "./lib/*:./app.jar", "ua.com.paw.OnlineShopApplication"]
