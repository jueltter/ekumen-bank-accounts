# Stage 1: Build the application using Gradle
FROM gradle:8.13.0-jdk21 AS build
WORKDIR /home/gradle/project
# Copy the Gradle wrapper and configuration files first for dependency caching
COPY gradlew gradlew.bat settings.gradle ./
COPY gradle gradle
# Copy the rest of the source code
COPY . .
# Build the project, skipping tests if desired
RUN ./gradlew clean bootJar --no-daemon -x test

# Stage 2: Create the runtime image using a lightweight JRE image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy the built jar from the build stage; adjust the jar name if necessary
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Expose the port that your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]