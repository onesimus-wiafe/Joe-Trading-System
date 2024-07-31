# Please build the Dockerfile from the main directory of the project using the following command:
# docker build -f ReportService/Dockerfile -t joe-trading/report-srv .

FROM eclipse-temurin:17-jdk-alpine AS deps

WORKDIR /build

COPY --chmod=0755 gradlew gradlew
COPY gradle/ gradle/

COPY build.gradle.kts settings.gradle.kts ./
COPY ./MarketDataService/build.gradle.kts ./MarketDataService/settings.gradle.kts ./MarketDataService/
COPY ./OrderProcessingService/build.gradle.kts ./OrderProcessingService/settings.gradle.kts ./OrderProcessingService/
COPY ./UserManagement/build.gradle.kts ./UserManagement/settings.gradle.kts ./UserManagement/
COPY ./ReportService/build.gradle.kts ./ReportService/settings.gradle.kts ./ReportService/
COPY ./shared/build.gradle.kts ./shared/settings.gradle.kts ./shared/

RUN ./gradlew dependencies --no-daemon

FROM deps AS build

WORKDIR /build

COPY ./ReportService/src ./ReportService/src/
COPY ./shared/src ./shared/src/

RUN ./gradlew :ReportService:build -x test --no-daemon && \
  mv ReportService/build/libs/$(./gradlew :ReportService:properties -q | grep '^name:' | awk '{print $2}')-$(./gradlew :ReportService:properties -q | grep '^version:' | awk '{print $2}').jar ./app.jar

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

COPY --from=build /build/app.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
