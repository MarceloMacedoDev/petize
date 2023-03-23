 # Use a JDK 17 base image from the Docker Hub
FROM openjdk:17-jdk-slim

 # Set the working directory
WORKDIR /app

 # Copy the source code to the container
COPY . /app

 # Compile the Java code
RUN ./mvnw clean package

 # Define a porta que o aplicativo escuta
EXPOSE 8080

ADD ./target/*.jar /app/app-runner.jar


# Defina as variáveis de ambiente
ENV APP_PROFILE test
ENV RABBITMQ_HOST docker-rabbitmq
ENV RABBITMQ_PORT 5672
ENV RABBITMQ_USERNAME admin
ENV RABBITMQ_PASSWORD 123456
ENV RABBITMQ_EXCHANGE petize.exchange
ENV RABBITMQ_QUEUE petize.queue
ENV RABBITMQ_ROUTINGKEY petize.routingkey
ENV HOST_MYSQL docker-mysql


 # Run the application
CMD ["java", "-jar", "/app/app-runner.jar"]
