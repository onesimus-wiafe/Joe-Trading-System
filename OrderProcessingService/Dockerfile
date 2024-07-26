FROM eclipse-temurin:17-jdk-alpine AS deps

WORKDIR /build

COPY --chmod=0755 gradlew gradlew
COPY gradle/ gradle/

COPY build.gradle.kts settings.gradle.kts ./

RUN ./gradlew dependencies --no-daemon

FROM deps AS build

WORKDIR /build

COPY ./src src/

RUN ./gradlew build -x test --no-daemon && \
    mv build/libs/$(./gradlew properties -q | grep '^name:' | awk '{print $2}')-$(./gradlew properties -q | grep '^version:' | awk '{print $2}').jar build/libs/app.jar

FROM eclipse-temurin:17-jre-alpine AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

COPY --from=build /build/build/libs/app.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
