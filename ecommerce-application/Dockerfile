FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} ecommerce-application.jar

ENTRYPOINT ["java", "-jar", "/ecommerce-application.jar"]

EXPOSE 9090