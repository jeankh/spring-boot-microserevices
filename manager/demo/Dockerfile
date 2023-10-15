
# Utiliser l'image Java 1.8 comme base
FROM openjdk:8-jre-alpine

WORKDIR /app


# Copier le fichier JAR exécutable dans l'image Docker (assurez-vous que le fichier JAR a le bon nom)
COPY target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

# Exposer le port 8761 que votre application Eureka Server utilisera
EXPOSE 8761

# Commande pour lancer l'application lorsque le conteneur démarre
CMD ["java", "-jar", "eureka-server.jar"]