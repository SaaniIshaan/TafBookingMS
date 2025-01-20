FROM amazoncorretto:17
WORKDIR /app
COPY build/libs/booking-app.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]