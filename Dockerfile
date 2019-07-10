FROM java:8
ADD target/spring-ticketingsystem-backend.jar spring-ticketingsystem-backend.jar

RUN mkdir -p src/main/resources/state_machine/templates
RUN mkdir -p src/main/resources/state_machine/xml_files

WORKDIR /
COPY ./src/main/resources/state_machine/templates/template_FSM.xml /src/main/resources/state_machine/templates
COPY ./src/main/resources/state_machine/xml_files/st.xml /src/main/resources/state_machine/xml_files

EXPOSE 8200
ENTRYPOINT ["java","-jar","spring-ticketingsystem-backend.jar"]
