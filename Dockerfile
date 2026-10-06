# Multi-stage Docker build for Spring Boot application

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Maven wrapper and configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Ensure wrapper script is executable
RUN chmod +x ./mvnw

# Copy source files and package application
COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2: Production runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Run as non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy built JAR from builder stage
COPY --from=builder /app/target/portfolio-backend-0.0.1-SNAPSHOT.jar portfolio-backend-0.0.1-SNAPSHOT.jar

# Port documentation (Spring Boot dynamically reads PORT via server.port=${PORT:8080})
EXPOSE 8080

# Execute Spring Boot application
ENTRYPOINT ["java", "-jar", "portfolio-backend-0.0.1-SNAPSHOT.jar"]
