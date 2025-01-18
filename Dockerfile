# Use a base image with OpenJDK
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /UserOrder

# Copy the JAR file from the host machine to the container
COPY target/UserOrderOperations-0.0.1-SNAPSHOT.jar /UserOrder/UserOrderOperations.jar

# Expose the port the app will run on (default for Spring Boot is 8080)
EXPOSE 8080

# Run the app using Java
ENTRYPOINT ["java", "-jar", "UserOrderOperations.jar"]
