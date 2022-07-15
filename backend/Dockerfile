 FROM adoptopenjdk/openjdk11:alpine-jre

 WORKDIR /opt/datalake

 ARG JAR_FILE=target/backend-1.11.1.jar
 COPY ${JAR_FILE} backend-1.11.1.jar


 ENTRYPOINT ["java","-jar","backend-1.11.1.jar"]