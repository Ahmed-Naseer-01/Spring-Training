# Use Maven with Java 21 for the build stage
FROM maven:latest AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files into the container
COPY pom.xml ./
COPY src ./src

# Run Maven to build the project and skip tests
RUN mvn clean package -DskipTests

# Use a slim Java 21 image for the runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage to the runtime stage
COPY --from=build /app/target/expense_reimbursement_system-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
