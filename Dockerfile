#0
FROM maven:latest as builder
COPY . .
RUN mvn install -DskipTests
RUN find / | grep clamav-rest-.*.jar

#1
FROM centos:centos7

MAINTAINER lokori <antti.virtanen@iki.fi>

RUN yum update -y && yum install -y java-1.8.0-openjdk &&  yum install -y java-1.8.0-openjdk-devel && yum clean all

# Set environment variables.
ENV HOME /root
ENV CLAMD_HOST 127.0.0.1
ENV CLAMD_PORT 3310
ENV MAXSIZE 10240MB
ENV TIMEOUT 10000

# Get the JAR file 
RUN mkdir /var/clamav-rest
COPY --from=0 /target/clamav-rest-1.0.2.jar /var/clamav-rest/clamav-rest-1.0.2.jar
#COPY target/clamav-rest-1.0.2.jar /var/clamav-rest/

# Define working directory.
WORKDIR /var/clamav-rest/

# Open up the server 
EXPOSE 8080

# ADD bootstrap.sh /
ENTRYPOINT ["java", "-jar", "/var/clamav-rest/clamav-rest-1.0.2.jar", "--clamd.host=${CLAMD_HOST}", "--clamd.port=${CLAMD_PORT}", "--clamd.maxfilesize=${MAXSIZE}", "--clamd.maxrequestsize=${MAXSIZE}", "--clamd.timeout=${TIMEOUT}", "--server.shutdown=graceful", "--spring.lifecycle.timeout-per-shutdown-phase=60s", "--debug"]
