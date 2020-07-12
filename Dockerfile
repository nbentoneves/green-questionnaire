FROM openjdk:8-jdk-alpine

EXPOSE 8080

ARG QUESTIONNAIRE=target/classes/questions.yml
ARG JAR_FILE=target/green-questionnaire-0.0.1-SNAPSHOT.jar

#Copy the questions inside the images
COPY ${QUESTIONNAIRE} questions.yml
ADD ${JAR_FILE} gree-questionnaire.jar

ENTRYPOINT ["java","-jar","-Dfile.question=questions.yml","/gree-questionnaire.jar"]