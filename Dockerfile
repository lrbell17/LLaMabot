FROM openjdk:17-jdk-slim
WORKDIR /app

ARG JAR_FILE=target/llamabot-*.jar
COPY ${JAR_FILE} llamabot.jar

COPY keycloak/wait-for-keycloak.sh .
RUN chmod +x wait-for-keycloak.sh

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "llamabot.jar"]
