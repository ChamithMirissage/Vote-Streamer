FROM amazoncorretto:21-alpine-jdk
COPY target/vote-streamer.jar vote-streamer.jar
EXPOSE 8080
ENTRYPOINT ["java",  "-jar", "vote-streamer.jar"]