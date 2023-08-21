FROM amazoncorretto:17-alpine-jdk
EXPOSE 9090 8080
COPY ecommerce-application/build/*.jar ecommerce-application.jar
ENTRYPOINT ["java","-jar","/ecommerce-application.jar"]