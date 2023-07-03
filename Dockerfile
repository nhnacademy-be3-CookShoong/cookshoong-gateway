FROM eclipse-temurin:11
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} cookshoong-gateway.jar

ENTRYPOINT ["java", "-jar", "/cookshoong-gateway.jar"]
