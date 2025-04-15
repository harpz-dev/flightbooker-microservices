# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy build artifacts
COPY build/libs/bookingservice-*.jar bookingservice.jar

# Expose application port
EXPOSE 5555

# Command to run the application
ENTRYPOINT ["java", "-jar", "bookingservice.jar"]
