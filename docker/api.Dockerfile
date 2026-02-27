FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

COPY . .
RUN chmod +x ./api/gradlew && ./api/gradlew -p /workspace :api:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/api/build/libs/*.jar /app/app.jar

EXPOSE 9999
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
