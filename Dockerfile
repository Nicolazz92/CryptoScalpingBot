#Where we start
FROM openjdk:14-alpine AS builder

#Set timezome
RUN apk add --no-cache tzdata
ENV TZ Europe/Moscow

#Get APK up to date
RUN apk update && apk upgrade

#Install Maven
RUN apk add maven

#Git
RUN apk add git
RUN mkdir /CryptoScalpingBot
RUN git clone -b master https://nicolazz92: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@github.com/Nicolazz92/CryptoScalpingBot.git /CryptoScalpingBot

#Build
RUN mvn -f /CryptoScalpingBot clean install -DskipTests=true

#Build release image
FROM openjdk:14-alpine

#Copy result
WORKDIR /Executables
COPY --from=builder /CryptoScalpingBot/trade-spot-scalper-strategy1/target/ .

#Add user and group for running as unprivileged user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

#Define how to start
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "trade-spot-scalper-strategy1-0.0.1-SNAPSHOT.jar"]