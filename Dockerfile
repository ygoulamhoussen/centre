# Utiliser l'image officielle de Java
FROM openjdk:17-slim

# Copier le fichier JAR généré dans l'image Docker
COPY centre-api/target/centre-api-0.0.1-SNAPSHOT.jar /usr/app/centre-api.jar

# Exposer le port 8080
EXPOSE 8080

# Définir le répertoire de travail
WORKDIR /usr/app

# Lancer l'application Spring Boot
CMD ["java", "-jar", "centre-api.jar"]
