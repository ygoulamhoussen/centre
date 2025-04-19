# Étape 1 : Construction de l'image pour le frontend
FROM node:18 AS frontend-build

WORKDIR /app/centre-ui

# Installer pnpm globalement
RUN npm install -g pnpm

# Copier les fichiers package pour installer les dépendances
COPY centre-ui/pnpm-lock.yaml ./ 
COPY centre-ui/package.json ./
# Copier les fichiers de dépendances locaux si besoin (monorepo)
COPY centre-ui/packages ./packages

RUN pnpm install --frozen-lockfile

# Copier le reste du code frontend
COPY centre-ui/ .

# Ajouter un tsconfig spécial build pour ignorer les tests
COPY centre-ui/tsconfig.build.json ./tsconfig.build.json

# Construire le frontend en utilisant ce tsconfig
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
