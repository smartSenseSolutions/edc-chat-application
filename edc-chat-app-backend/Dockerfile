FROM gradle:8.8-jdk21-alpine AS build
LABEL maintainer="smartSense"
COPY --chown=gradle:gradle ./edc-chat-app-backend/ /home/app
WORKDIR /home/app
RUN gradle clean build -i -x test -x javadoc

FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="smartSense"
COPY --from=build /home/app/build/libs/edc-chat-backend-latest.jar /usr/local/lib/backend/app.jar
RUN apk update && apk upgrade libssl3 libcrypto3
RUN addgroup -S adapter && adduser -S adapter -G adapter
USER adapter
WORKDIR /usr/local/lib/backend
EXPOSE 8080
CMD java -jar app.jar
