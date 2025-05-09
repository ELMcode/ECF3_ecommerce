# Use Java 11 for compilation
FROM maven:3.8.4-openjdk-11-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml to download dependencies
COPY pom.xml .

# Download all dependencies without building
RUN mvn dependency:go-offline -B

# Copy sources
COPY src ./src

# Build with Lombok parameters
RUN mvn package -DskipTests

# Use Java 11 for final image
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# Declare environment variables with empty defaults
ENV PORT=""
ENV ACTIVE_PROFILE=""
ENV REDIS_HOST=""
ENV REDIS_PORT=""
ENV REDIS_PASSWORD=""

# Expose port
EXPOSE ${PORT}

# Command to run application
ENTRYPOINT ["java", "-jar", "app.jar"]
