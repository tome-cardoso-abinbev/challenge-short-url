# Maven image to build the application
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY ./src ./src
RUN mvn clean package -DskipTests

# OpenJDK image to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy of the built Spring Boot application
COPY --from=build /app/target/challenge-short-url-0.0.1-SNAPSHOT.jar /app/app.jar

# Install NGINX
RUN apt-get update && apt-get install -y nginx

# Copy NGINX configuration
COPY env/nginx.conf /etc/nginx/nginx.conf

# Expose port 80 for NGINX
EXPOSE 80

# Start NGINX and the application
CMD ["sh", "-c", "nginx && java -jar /app/app.jar"]