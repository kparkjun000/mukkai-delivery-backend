# Use OpenJDK 11
FROM openjdk:11-jdk-slim

# Install required packages
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew ./
COPY gradle/ ./gradle/
COPY build.gradle ./
COPY settings.gradle ./

# Copy source code
COPY api/ ./api/
COPY db/ ./db/
COPY common/ ./common/
COPY store-admin/ ./store-admin/
COPY src/ ./src/

# Make gradlew executable
RUN chmod +x ./gradlew

# Build application
RUN ./gradlew build -x test --no-daemon

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-Dserver.port=${PORT}", "-Dspring.profiles.active=railway", "-jar", "build/libs/delivery-app.jar"]