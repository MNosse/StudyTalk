FROM gradle:8.2.1-jdk17 AS build

COPY . /studytalk

WORKDIR /studytalk

RUN gradle clean build

FROM openjdk:17-slim

COPY --from=build /studytalk/build/libs/studytalk-0.0.1-SNAPSHOT.jar /app/studytalk.jar

WORKDIR /app

CMD ["java", "-jar", "studytalk.jar"]