FROM ubuntu:latest AS build

# Instala JDK e Maven
RUN apt-get update && apt-get install -y openjdk-21-jdk maven

# Define diretório de trabalho
WORKDIR /app

# Copia o conteúdo da pasta backend (onde está o Dockerfile e o pom.xml) para /app
COPY . .

# Compila o projeto
RUN mvn clean install -DskipTests

# ---------------------------
# Stage de execução
# ---------------------------
FROM openjdk:21-jdk

WORKDIR /app

EXPOSE 8080

# Copia o JAR gerado no build
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
