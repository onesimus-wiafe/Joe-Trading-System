FROM gradle:jdk17-focal AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine

COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
