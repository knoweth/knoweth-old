# Start with a base image containing Java runtime
FROM openjdk:11

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/knoweth-1.0-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} knoweth.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","/knoweth.jar"]

