# Étape 1 : build Maven avec JDK 21 (Temurin)
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY certs/ca.crt /tmp/ca.crt
RUN keytool -importcert -noprompt -trustcacerts \
    -alias internal-ca \
    -file /tmp/ca.crt \
    -keystore "$JAVA_HOME/lib/security/cacerts" \
    -storepass changeit

COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B clean package -DskipTests

# Étape 2 : image finale avec JRE 21 (Temurin)
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
