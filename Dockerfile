FROM amazoncorretto:17-alpine-jdk
LABEL maintainer = "Valentin Manfredi"
COPY target/URLShortenervnm-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar" ]