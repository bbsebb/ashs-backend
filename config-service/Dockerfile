FROM openjdk:21-oracle
VOLUME /tmp
COPY build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar", "app.jar"]


