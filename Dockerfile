FROM centos:6

MAINTAINER lokori <antti.virtanen@iki.fi>

RUN yum update -y 
RUN yum install -y java-1.8.0-openjdk

# jar command
RUN yum install -y java-1.8.0-openjdk-devel

RUN yum clean all

# Set environment variables.
ENV HOME /root

# Get the JAR file 
CMD mkdir /var/clamav-rest
COPY clamav-rest-1.0.0.jar /var/clamav-rest/

# Define working directory.
WORKDIR /var/clamav-rest/

# Open up the server 
EXPOSE 8080

ADD bootstrap.sh /
ENTRYPOINT ["/bootstrap.sh"]

# docker build -t lokori/clamav-rest .