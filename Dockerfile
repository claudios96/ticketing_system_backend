FROM java:8
ADD target/spring-ticketingsystem-backend.jar spring-ticketingsystem-backend.jar

RUN mkdir risorseProgetto

WORKDIR /
COPY ./src/main/resources/ /risorseProgetto/

EXPOSE 8200
ENTRYPOINT ["java","-jar","spring-ticketingsystem-backend.jar"]
