# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Add the Maven wrapper and pom.xml files to leverage Docker's layer caching
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine

# Add a non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Set the working directory and copy the built JAR from the build stage
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Change ownership and permissions
RUN chown -R spring:spring /app && chmod +x /app/app.jar

# Switch to the non-root user
USER spring:spring

# Expose the application port
EXPOSE 8080

# JVM options can be customized using environment variables
ENV JAVA_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]