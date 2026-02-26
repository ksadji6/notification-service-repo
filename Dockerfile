# Étape 1 : Build avec Maven et Java 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Installation Maven
RUN apk add --no-cache maven

# Préparation des dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Build du JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copie du JAR
COPY --from=build /app/target/notification-service-*.jar app.jar

# On utilise le port 8087 comme convenu
EXPOSE 8087

# Optimisation mémoire
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]