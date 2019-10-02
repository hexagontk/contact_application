
FROM openjdk:11
ENV PROJECT hexagon-contact-application

COPY build/install/$PROJECT /opt/$PROJECT
WORKDIR /opt/$PROJECT
EXPOSE 9090
ENTRYPOINT /opt/$PROJECT/bin/$PROJECT
