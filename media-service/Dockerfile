FROM openjdk:11
LABEL authors="Hasten John"
EXPOSE 9082
WORKDIR /app
ADD media-biz-0.0.1-SNAPSHOT.jar /app/media-biz-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=prod", "-jar", "/app/media-biz-0.0.1-SNAPSHOT.jar"]

