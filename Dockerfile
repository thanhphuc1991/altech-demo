# Use OpenJDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set workdir
WORKDIR /app

# Copy and build app
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]