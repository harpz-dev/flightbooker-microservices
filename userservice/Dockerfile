# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy build artifacts
COPY build/libs/userservice-*.jar userservice.jar

# Expose application port
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "userservice.jar"]
