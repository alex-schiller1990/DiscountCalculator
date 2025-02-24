# Stage 1: Build the application
FROM eclipse-temurin:23-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# Stage 2: Create the final image
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/discountCalculator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
