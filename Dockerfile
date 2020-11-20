FROM openjdk:15-alpine
WORKDIR /app
COPY /target/EmailServer-1.0.jar /app

CMD ["java", "-jar", "/app/EmailServer-1.0.jar"]