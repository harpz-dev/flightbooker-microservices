# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy build artifacts
COPY build/libs/flightservice-*.jar flightservice.jar

# Expose application port
EXPOSE 4545

# Command to run the application
ENTRYPOINT ["java", "-jar", "flightservice.jar"]
