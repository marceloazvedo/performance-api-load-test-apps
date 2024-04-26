FROM azul/zulu-openjdk-alpine:17-latest

ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
