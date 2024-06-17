FROM amazoncorretto:21
COPY /target/wordsearch-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
