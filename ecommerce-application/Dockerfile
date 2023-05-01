FROM openjdk:17

ARG JAR_FILE=build/libs/ecommerce-application-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} ecommerce-application.jar

ENTRYPOINT ["java", "-jar", "/ecommerce-application.jar"]

EXPOSE 9090