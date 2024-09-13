FROM openjdk:11
LABEL authors="Hasten John"
EXPOSE 9081
WORKDIR /app
ADD content-biz-0.0.1-SNAPSHOT.jar /app/content-biz-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=prod", "-jar", "/app/content-biz-0.0.1-SNAPSHOT.jar"]

