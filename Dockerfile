# Estágio 1: Build
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM amazoncorretto:17-al2-jdk
# O nome do JAR pode variar, esse comando garante que pegamos o correto
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]