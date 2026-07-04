FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

COPY src ./src

RUN ./mvnw -DskipTests package

FROM eclipse-temurin:25-jre

WORKDIR /app

RUN groupadd --system assetflow && useradd --system --gid assetflow assetflow

COPY --from=build /app/target/assetflow-backend-0.0.1-SNAPSHOT.jar app.jar

USER assetflow

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
