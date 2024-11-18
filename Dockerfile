FROM gradle:8.10.2-jdk21-alpine AS build
COPY --chown=gradle:gradle . /home/app
WORKDIR /home/app
RUN gradle clean build --no-daemon -i -x test -x javadoc

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /home/app/chat-web/build/libs/chat-web.jar /usr/local/lib/edc-chat/app.jar
RUN apk update && apk upgrade libssl3 libcrypto3
RUN addgroup -S edc-chat && adduser -S edc-chat -G edc-chat
USER edc-chat
WORKDIR /usr/local/lib/edc-chat
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
