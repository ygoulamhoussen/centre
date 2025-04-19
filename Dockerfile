# Étape 1 : Construction de l'image pour le frontend
FROM node:18 AS frontend-build

WORKDIR /app/centre-ui

# Installer pnpm globalement
RUN npm install -g pnpm
# Copier le reste du code frontend
COPY centre-ui/ .

# Installer les dépendances sans interactivité
RUN pnpm install --frozen-lockfile




# Construire le frontend
RUN pnpm run build

# Étape 2 : Construction de l'image pour le backend
FROM openjdk:17-jdk-slim AS backend-build

WORKDIR /app/centre-api

# Installer Maven
RUN apt-get update && apt-get install -y maven

# Copier le backend
COPY centre-api /app/centre-api

# Copier le build frontend
COPY --from=frontend-build /app/centre-ui/dist /app/centre-api/src/main/resources/static

# Build backend (sans tests)
RUN mvn clean install -DskipTests

# Étape 3 : Image finale pour l'exécution
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le JAR généré
COPY --from=backend-build /app/centre-api/target/centre-api-0.0.1-SNAPSHOT.jar /app/centre-api.jar

# Lancer l'application
CMD ["java", "-jar", "/app/centre-api.jar"]
