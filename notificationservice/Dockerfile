# Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR into the container
COPY build/libs/notificationservice-*.jar notificationservice.jar

# Expose the port (if you use 8082 or another, adjust accordingly)
EXPOSE 8085

# Command to run the application
ENTRYPOINT ["java", "-jar", "notificationservice.jar"]
