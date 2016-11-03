FROM centos:6

MAINTAINER lokori <antti.virtanen@iki.fi>

RUN yum update -y && yum install -y java-1.8.0-openjdk &&  yum install -y java-1.8.0-openjdk-devel && yum clean all

# Set environment variables.
ENV HOME /root

# Get the JAR file 
CMD mkdir /var/clamav-rest
COPY clamav-rest-1.0.2.jar /var/clamav-rest/

# Define working directory.
WORKDIR /var/clamav-rest/

# Open up the server 
EXPOSE 8080

ADD bootstrap.sh /
ENTRYPOINT ["/bootstrap.sh"]

