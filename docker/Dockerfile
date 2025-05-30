# Build stage
FROM eclipse-temurin:17-jdk AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better layer caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

# Create app user for security
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Set working directory
WORKDIR /app

# Copy the JAR from build stage
COPY --from=builder /app/target/issue-reporting-0.0.1-SNAPSHOT.jar app.jar

# Create uploads directory and set permissions
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port 8081 (matching your application.properties)
EXPOSE 8081

# Health check (curl might not be available in JRE image, so we'll use a simple Java check)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
CMD java -cp app.jar org.springframework.boot.loader.JarLauncher --server.port=8081 --management.endpoints.web.exposure.include=health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]