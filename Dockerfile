FROM centos:6

MAINTAINER lokori <antti.virtanen@iki.fi>

RUN yum update -y 
RUN yum install -y java-1.8.0-openjdk

# jar command
RUN yum install -y java-1.8.0-openjdk-devel

RUN yum clean all

# Set environment variables.
ENV HOME /root

# where is the JAR
# docker build -t lokori/clamav-rest .
# VOLUME [ "/var/clamav-rest" ]

# Get the JAR file 
CMD mkdir /var/clamav-rest
COPY clamav-rest-1.0.0.jar /var/clamav-rest/

# Define working directory.
WORKDIR /var/clamav-rest

# Open up the server 
EXPOSE 8080

# Define default command.
# defaults.put("clamd.host", "192.168.50.72");
# docker run -t -i lokori/clamav-rest -d clamd.host=fofoa
CMD ["--clamd.host=192.168.50.72"]
ENTRYPOINT java -jar /var/clamav-rest/clamav-rest-1.0.0.jar
