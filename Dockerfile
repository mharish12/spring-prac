FROM amazoncorretto:17-alpine-jdk
COPY ecommerce-application/build/*.jar ecommerce-application.jar
ENTRYPOINT ["java","-jar","/ecommerce-application.jar"]