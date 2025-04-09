# Utilisez une image de base pour Java
FROM openjdk:17-jdk-slim

# Définissez le répertoire de travail
WORKDIR /app

# Copiez le JAR généré dans l'image Docker
COPY target/centre-api-0.0.1-SNAPSHOT.jar app.jar

# Exposez le port utilisé par Spring Boot
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]