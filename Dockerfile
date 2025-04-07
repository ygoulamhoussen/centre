# Étape 1 : Construction de l'image pour le frontend
FROM node:18 AS frontend-build

WORKDIR /app/centre-ui

# Copier les fichiers du projet frontend dans le conteneur
COPY centre-ui/package*.json ./

# Installer les dépendances frontend (npm install)
RUN npm install

# Copier le reste des fichiers
COPY centre-ui/ .

# Exécuter la commande de build du frontend
RUN npm run build

# Étape 2 : Construction de l'image pour le backend
FROM openjdk:17-jdk-slim AS backend-build

WORKDIR /app/centre-api

# Installer Maven dans l'image
RUN apt-get update && apt-get install -y maven

# Copier le code backend dans le conteneur
COPY centre-api /app/centre-api

# Copier les fichiers générés du frontend dans le backend
COPY --from=frontend-build /app/centre-ui/dist /app/centre-api/src/main/resources/static

# Installer les dépendances backend et construire le projet
RUN mvn clean install -DskipTests

# Étape 3 : Construction de l'image finale pour l'exécution
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le fichier JAR généré depuis l'étape précédente
COPY --from=backend-build /app/centre-api/target/centre-api-0.0.1-SNAPSHOT.jar /app/centre-api.jar

# Définir l'entrée pour l'application (par exemple, démarrer l'application backend)
CMD ["java", "-jar", "/app/centre-api.jar"]
