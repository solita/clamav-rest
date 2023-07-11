#0
FROM maven:latest as builder
COPY . .
RUN mvn install -DskipTests
RUN find / | grep clamav-rest-.*.jar

#1
FROM eclipse-temurin:17

MAINTAINER lokori <antti.virtanen@iki.fi>

# Set environment variables.
# Get the JAR file 
RUN mkdir /var/clamav-rest
ENV HOME /var/clamav-rest
COPY --from=0 /target/clamav-rest-1.1.0.jar /var/clamav-rest/clamav-rest-1.1.0.jar

# Define working directory.
WORKDIR /var/clamav-rest/
RUN chmod 777 /var/clamav-rest
RUN chmod 644 /var/clamav-rest/clamav-rest-1.1.0.jar
ADD bootstrap.sh /
RUN chmod 755 /bootstrap.sh

RUN adduser --disabled-password  --gecos "First Last,RoomNumber,WorkPhone,HomePhone" --home /var/clamav-rest --shell /bin/bash --uid 1000 app
RUN chown -R 1000:1000 /var/clamav-rest
# Open up the server 
EXPOSE 8080

USER 1000

ENTRYPOINT ["/bootstrap.sh"]

