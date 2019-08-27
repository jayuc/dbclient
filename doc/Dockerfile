FROM ubuntu18/jre8:v1.1.2
MAINTAINER yujie
ENV REFRESHED_AT 2019-08-27
WORKDIR /usr
RUN mkdir app
ADD dbclient-2.1.5.jar /usr/app/
ADD application.properties /usr/app/
Volume /app/log
Volume /app/feedback
EXPOSE 8004
ENTRYPOINT ["sh","-c","java -jar -Dspring.config.location=/usr/app/application.properties /usr/app/dbclient-2.1.5.jar"]
