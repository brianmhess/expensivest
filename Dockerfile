#####################
# Simple Dockerfile #
#####################

FROM maven:3.5-jdk-8-alpine as builder

COPY target/expensivest*.jar /expensivest.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar","/expensivest.jar"]
