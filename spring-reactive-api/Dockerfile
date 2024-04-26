FROM azul/zulu-openjdk-alpine:17-latest

ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar

ENV DB_MAXIMUM_POOL_SIZE=100
ENV DB_MINIMUM_IDLE=10

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar","-Ddb_maximum_pool_size=${DB_MAXIMUM_POOL_SIZE}", "-Ddb_minimum_idle=${DB_MINIMUM_IDLE}"]
