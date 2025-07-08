# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM openjdk:21-jre-slim

# Set working directory
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Configure the server port
ENV SERVER_PORT=8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=8080"]
