FROM amazoncorretto:17-alpine-jdk
COPY ecommerce-application/build/
ENTRYPOINT ["java","-jar","/message-server-1.0.0.jar"]